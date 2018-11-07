package com.mmall.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.mmall.config.UserInfoConfig;
import com.mmall.constants.LevelConstants;
import com.mmall.dao.PricingGroupMapper;
import com.mmall.dao.SpecialPricingGroupMapper;
import com.mmall.dao.TotalMapper;
import com.mmall.dto.BillDto;
import com.mmall.dto.ProfitsDto;
import com.mmall.excel.Bill;
import com.mmall.excel.export.DataSheetExecute;
import com.mmall.excel.export.ExcelExportExecutor;
import com.mmall.excel.imp.XlsxProcessAbstract;
import com.mmall.model.*;
import com.mmall.model.Response.InfoEnums;
import com.mmall.model.Response.Result;
import com.mmall.model.params.BillDetailsParam;
import com.mmall.model.params.BillParam;
import com.mmall.model.params.TotalIncomeParam;
import com.mmall.service.*;
import com.mmall.util.DateUtils;
import com.mmall.util.LevelUtil;
import com.mmall.util.RandomHelper;
import com.mmall.util.UploadApi;
import com.mmall.vo.PricingGroupVo;
import com.mmall.vo.TotalVo;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * <p>
 * 月计表(客户账单) 服务实现类
 * </p>
 *
 * @author qty
 * @since 2018-09-25
 */
@Service
public class TotalServiceImpl extends ServiceImpl<TotalMapper, Total> implements TotalService {

    @Autowired
    private TotalMapper totalMapper;

    @Autowired
    private SysUserInfoService sysUserInfoService;


    @Autowired
    private PricingGroupMapper pricingGroupMapper;

    @Autowired
    private ProvinceCalculateService provinceCalculateService;

    @Autowired
    private SpecialPricingGroupMapper specialPricingGroupMapper;

    @Autowired
    private TotalService totalService;

    @Autowired
    private WeightCalculateService weightCalculateService;

    @Autowired
    private DailyTotalService dailyTotalService;

    //成本
    private static  BigDecimal totalCost=BigDecimal.ZERO;

    //报价
    private static  BigDecimal totalOffer=BigDecimal.ZERO;

    //设置导出路径（生产环境无效）
//    private static String path="E:/GDW/";

    /**
     * 获取客户月计统计
     * @return
     */
    public BillDto getBillData(BillParam billParam) {

        if(billParam.getUserId()==null||"".equals(billParam.getUserId())){
            billParam.setUserId(getUserIdStr()+",-1");
        }

        SysUserInfo userInfo = UserInfoConfig.getUserInfo();

        if(userInfo.getPlatformId()==3){
            userInfo.setId(0);
        }

        Total one = totalMapper.getToal(billParam.getDate(), billParam.getUserId(),userInfo.getId());

        if(one==null){
            return new BillDto();
        }
        BillDto billDto=new BillDto();
        billDto.setTotalNumber(one.getTotalNumber());
        billDto.setTotalWeight(one.getTotalWeight());
        billDto.setAverageWeight(one.getTotalWeight().divide(new BigDecimal(one.getTotalNumber()),2, RoundingMode.DOWN));

        int days = DateUtils.getDays(one.getTotalTime());
        billDto.setDailyNum(one.getTotalNumber()/days);

        return billDto;
    }



    /**
     * 获取客户对应的账单
     * @param totalTime
     * @param userId
     * @return
     */
    public List<Total> listToal(String totalTime, String userId) {
        return totalMapper.listToal(totalTime,userId);
    }



    /**
     * 获取公司账单----数据分析
     * @return
     */
    public IPage<TotalVo> getBill(IPage<TotalVo> page, BillDetailsParam billDetailsParam,Integer type) {

        SysUserInfo userInfo = UserInfoConfig.getUserInfo();

        String nameStr=getUserIdStr();

        if(!"".equals(billDetailsParam.getUserId()) && billDetailsParam.getUserId()!=null){
            nameStr=billDetailsParam.getUserId().toString();
        }
        Page<TotalVo> bill=null;
        if(type==2){
            bill = totalMapper.getBill(page,billDetailsParam.getDate(),nameStr, billDetailsParam.getState(),type,null);
        }else{
            bill = totalMapper.getBill(page,billDetailsParam.getDate(),nameStr, billDetailsParam.getState(),type,userInfo.getId());
        }
        return bill;
    }

    /**
     * 获取已收和未收金额
     * @return
     */
    public TotalIncomeParam getBillCount(String date) {

        String nameStr=getUserIdStr();

        String staStr="-1,1,2,3";
        String steStr="4";
        Total totalPaid = totalMapper.getBillCount(date, nameStr, steStr);
        Total totalOffer = totalMapper.getBillCount(date, nameStr, staStr);

        TotalIncomeParam totalIncomeParam=new TotalIncomeParam();
        if(totalPaid!=null && totalPaid.getTotalPaid()!=null){
            totalIncomeParam.setTotalPaid(totalPaid.getTotalPaid());
        }
        if(totalOffer!=null && totalOffer.getTotalOffer()!=null){
            totalIncomeParam.setTotalOffer(totalOffer.getTotalOffer());
        }
        return totalIncomeParam;
    }

    /**
     * 利润分析
     * @param billParam
     * @return
     */
    public ProfitsDto getProfits(BillParam billParam) {

        if(billParam.getUserId()==null||"".equals(billParam.getUserId())){
            billParam.setUserId(getUserIdStr()+",-1");
        }

        SysUserInfo userInfo = UserInfoConfig.getUserInfo();
        Total one = totalMapper.getToal( billParam.getDate(), billParam.getUserId(),userInfo.getId());

        if(one==null){
            return null;
        }

        ProfitsDto billDto=new ProfitsDto();
        billDto.setTotalNumber(one.getTotalNumber());
        billDto.setTotalWeight(one.getTotalWeight());
        billDto.setAverageWeight(one.getTotalWeight().divide(new BigDecimal(one.getTotalNumber()),2, RoundingMode.DOWN));
        billDto.setTotalOffer(one.getTotalOffer().add(one.getTotalAdditional()));
        billDto.setTotalPaid(one.getTotalPaid());
        billDto.setTotalCost(one.getTotalCost());
        billDto.setProfits(billDto.getTotalPaid().subtract(one.getTotalCost()));
        billDto.setPrice(one.getTotalOffer().divide(new BigDecimal(one.getTotalNumber()),2,RoundingMode.DOWN));
        billDto.setCostPrice(one.getTotalCost().divide(new BigDecimal(one.getTotalNumber()),2,RoundingMode.DOWN));

        int days = DateUtils.getDays(one.getTotalTime());
        billDto.setAverageNumber(one.getTotalNumber()/days);
        return billDto;
    }

    /**
     * 定价
     * @param totalId
     * @return
     */
    @Transactional
    public Result<String> getPricing(Integer totalId,Integer type) {

        //获取账单信息
        Total total = totalMapper.selectById(totalId);

        //判断是否能够定价
        if(total.getTotalState()>1){
            return Result.error(InfoEnums.TOATL_IS_PRICING);
        }

        if(total==null){
            return  Result.error(InfoEnums.BILL_IS_NULL);
        }

        //獲取报价表
        List<PricingGroupVo> pricingGroup = pricingGroupMapper.ListPricingGroup(total.getUserId());

        //获取特殊报价表
        List<PricingGroupVo> special = specialPricingGroupMapper.getPricingGroupVo(total.getUserId());

        //定价表是否数据存在
        List<Integer> allPricingGroups = pricingGroupMapper.getAllPricingGroups(total.getUserId());
        if(allPricingGroups.size()!=34){
            return Result.error(InfoEnums.PROCING_IS_NULL);
        }

        String[] str=total.getTotalUrl().split("/");

        //读取本地数据
        XlsxProcessAbstract xls=new XlsxProcessAbstract();
        ArrayListMultimap<String, Bill> map=null;
        try {
            map = xls.processAllSheet(total.getCdUrl());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(map==null){
            return null;
        }

        //创建bill集合
        List<Bill> list=new ArrayList<Bill>();

        for (String key:map.keySet()) {
            list.addAll(map.get(key));
        }

        if(type==2){

            //校验成本表是否存在数据
            List<Integer> ag = pricingGroupMapper.getAllPricingGroups(total.getSendId());
            if(ag.size()!=34){
                return Result.error(InfoEnums.COST_IS_NULL);
            }

            //获取成本表
            List<PricingGroupVo> pricingOffer = pricingGroupMapper.ListPricingGroup(total.getSendId());

            //获取特殊成本表
            List<PricingGroupVo> special1 = specialPricingGroupMapper.getPricingGroupVo(total.getSendId());

            //计算成本
            list= getCalculate(pricingOffer,1,list,special1);

        }

        //计算报价
        list=getCalculate(pricingGroup,2,list,special);

        //写入Excel
        String[] strings = {"商家名称", "扫描时间", "运单编号", "目的地", "快递重量","报价"};

        DataSheetExecute<Bill> dataSheetExecute = new DataSheetExecute<Bill>() {

//            public void execute(Row row, Bill personUser) {
//                row.createCell(0).setCellValue(personUser.getBillName());
//                row.createCell(1).setCellValue(personUser.getSweepTime());
//                row.createCell(2).setCellValue(personUser.getSerialNumber());
//                row.createCell(3).setCellValue(personUser.getDestination());
//                row.createCell(4).setCellValue(personUser.getWeight().toString());
//            }

            public void writeExcel(SXSSFWorkbook workbook, OutputStream outputStream) throws Exception {
                outputStream = new FileOutputStream(total.getCdUrl());
                workbook.write(outputStream);
                outputStream.close();
                workbook.close();
            }
        };

        new ExcelExportExecutor<Bill>(strings, list, dataSheetExecute, true).execute();

        //上传到oss
        File file=new File(total.getCdUrl());
        String upload = UploadApi.upload(file, str[str.length-1], total.getTotalTime()+"/"+total.getName()+"/");

        Total total1=new Total();
        total1.setTotalId(totalId);
        total1.setTotalCost(totalCost);
        total1.setTotalOffer(totalOffer);
        total1.setTotalState(1);
        total1.setTotalUrl(upload);
        total1.setUpdateTime(new Date());

        totalMapper.updateById(total1);
        totalCost=BigDecimal.ZERO;
        totalOffer=BigDecimal.ZERO;
        map.clear();
        return Result.ok(upload);
    }

    @Override
    public Result polling(String time, Integer id,String fileName) {
        fileName = fileName.split("\\.")[0];
        List<Total> totals = totalMapper.getTotals(time, id,fileName);
        Multimap<String, Total> totalMap = ArrayListMultimap.create();
        String existence = "existence";//已存在账单
        String other = "other";//其他账单
        for(Total total:totals){
            totalMap.put(total.getUserId()>0?existence:other,total);
        }
        Map<String,List<Total>> map = Maps.newHashMap();
        //首重集合
        List<Total> existenceList = (List<Total>) totalMap.get(existence);
        //续重集合
        List<Total> otherList = (List<Total>) totalMap.get(other);

        map.put(existence,existenceList);
        map.put(other,otherList);
        return Result.ok(map);
    }

    /**
     * 根据当前登录用户，获取该公司下所有用户
     * @return
     */
    public String getUserIdStr(){
        SysUserInfo user = UserInfoConfig.getUserInfo();

        //判断是否是客户
        if(user.getPlatformId()==3){
            return user.getId().toString();
        }
        String s = LevelUtil.calculateLevel(user.getLevel(), user.getId());
        List<SysUserInfo> list1 = sysUserInfoService.list(new QueryWrapper<SysUserInfo>()
                .like("level", s)
                .eq("platform_id", 3)
                .select("id"));

        String nameStr="";
        for(SysUserInfo sysUserInfo: list1){
            nameStr+=sysUserInfo.getId()+",";
        }

        nameStr=nameStr.substring(0,nameStr.length()!=0?nameStr.length()-1:0);
        return nameStr;
    }

    @Override
    public Result getBillDetails(Integer status,SysUserInfo userInfo, String userId,String date, Page ipage) {
        Total sumBiLLDetails = totalMapper.getSumBiLLDetails( status,userId, date, userInfo.getId());
        totalMapper.getAllBySendIdAndCreateTimeAndUserIds(ipage,status,userId,date,userInfo.getId());
        Map<String,Object> map = Maps.newHashMap();
        if(sumBiLLDetails==null){
            sumBiLLDetails = new Total();
            sumBiLLDetails.setTotalOffer(BigDecimal.ZERO);
            sumBiLLDetails.setTotalPaid(BigDecimal.ZERO);
        }
        map.put("sum",sumBiLLDetails);
        map.put("billDetails",ipage);
        return Result.ok(map);
    }

    @Override
    @Transactional
    public Result othersBillForward(String billIds, Integer userId) {
        List<Total> allBillByIds = totalMapper.getAllBillByIds(billIds);
        for(Total total:allBillByIds){
            total.setUserId(userId);
            totalMapper.updateById(total);
        }
        return Result.ok();
    }

    /**
     * 删除订单
     * @param totalId
     * @return
     */
    @Override
    @Transactional
    public Result deleteTotal(Integer totalId) {
        boolean totalId1 = totalService.remove(new UpdateWrapper<Total>().eq("total_id", totalId));
        if(totalId1){
            weightCalculateService.remove(new UpdateWrapper<WeightCalculate>().eq("total_id", totalId));
            provinceCalculateService.remove(new UpdateWrapper<ProvinceCalculate>().eq("total_id", totalId));
            dailyTotalService.remove(new UpdateWrapper<DailyTotal>().eq("total_id", totalId));
        }
        return Result.ok();
    }

    /**
     * 获取收款
     * @return
     */
    @Override
    public List<SysUserInfo> getCollection() {
        SysUserInfo user = UserInfoConfig.getUserInfo();
        return totalMapper.getCollection(user.getId());
    }

    /**
     * 预算
     * @param weight
     * @return
     */
    @Override
    public Result<List<Bill>> getBudget(Double weight,Integer userId) {

        //獲取报价表
        List<PricingGroupVo> pricingGroup = pricingGroupMapper.ListPricingGroup(userId);

        //获取特殊报价表
        List<PricingGroupVo> special = specialPricingGroupMapper.getPricingGroupVo(userId);

        //定价表是否数据存在
        List<Integer> allPricingGroups = pricingGroupMapper.getAllPricingGroups(userId);

        //判断定价组参数是否完善
        /*if(allPricingGroups.size()!=34){
            return Result.error(InfoEnums.PROCING_IS_NULL);
        }*/

        String[] str= LevelConstants.PROSTR;

        //格式化数据
        BigDecimal bd=new BigDecimal(weight.toString());

        //制造数据
        List<Bill> list=new ArrayList<>();
        for(String s:str){
            Bill b=new Bill();
            b.setDestination(s);
            b.setWeight(bd);
            list.add(b);
        }

        //计算报价
        list=getCalculate(pricingGroup,2,list,special);

        return Result.ok(list);
    }

    /**
     * 计算价格
     * @param pricingGroupVo 计算表
     * @param type  计算类型
     * @param list  计算后的数据
     * @return
     */
    @Transactional
    public List<Bill> getCalculate(List<PricingGroupVo> pricingGroupVo,
                                   Integer type,
                                   List<Bill> list,
                                   List<PricingGroupVo> special){

        //首重集合
        List<PricingGroupVo> first=new ArrayList<PricingGroupVo>();

        //特殊定价表首重集合
        List<PricingGroupVo> specialFirst=new ArrayList<PricingGroupVo>();

        //追加首重集合
        List<PricingGroupVo>  firstHeavy=new ArrayList<PricingGroupVo>();

        //续重集合
        List<PricingGroupVo> Continued=new ArrayList<PricingGroupVo>();

        //特殊定价表续重集合
        List<PricingGroupVo> specialContinued=new ArrayList<PricingGroupVo>();

        //追加首重集合
        List<PricingGroupVo>  additionalHeavy=new ArrayList<PricingGroupVo>();

        //分离首重和续重
        for(PricingGroupVo p:pricingGroupVo){

            //首重
            if(p.getFirstOrContinued()==1){
                first.add(p);
                continue;
            }

            if(p.getFirstOrContinued()==2){
                Continued.add(p);
            }
        }

        //分离特殊定价表首重和续重
        for(PricingGroupVo p:special){

            //特殊首重
            if(p.getFirstOrContinued()==1 && p.getStatus()==1){
                specialFirst.add(p);
                continue;
            }

            //特殊续重
            if(p.getFirstOrContinued()==2 && p.getStatus()==1){
                specialContinued.add(p);
                continue;
            }

            //追加首重
            if(p.getFirstOrContinued()==1 && p.getStatus()==2){
                firstHeavy.add(p);
                continue;
            }

            //追加续重
            if(p.getFirstOrContinued()==2 && p.getStatus()==2){
                additionalHeavy.add(p);
            }
        }

        //获取每一条账单数据
        for (Bill bill:list) {

            Boolean traverse=false;

            //遍历特殊定价组
            if(specialFirst.size()>0){
                traverse = traverse(bill,specialFirst, specialContinued, type);
            }

            //遍历定价组
            if(!traverse){
                traverse(bill,first,Continued,type);
            }

            //遍历追加的城市
            if(firstHeavy.size()>0 || additionalHeavy.size()>0){
                additional(bill,firstHeavy,additionalHeavy,type);
            }
        }

        return list;
    }

    /**
     * 根据首重和续重计算价格
     * @param bill
     * @param first
     * @param Continued
     */
    @Transactional
    public Boolean traverse(Bill bill,List<PricingGroupVo> first,List<PricingGroupVo> Continued,Integer type){

        //遍历首重
        for(PricingGroupVo pg: first){

            //根据城市锁定价格计算规则
            if(bill.getDestination().startsWith(pg.getCity())){

                //和区间开始比较
                int greater=bill.getWeight().compareTo(new BigDecimal(pg.getAreaBegin().toString()));

                //和区间结束大小比较
                int less=bill.getWeight().compareTo(new BigDecimal(pg.getAreaEnd().toString()));

                //在区间，计算首重
                if(greater>=0 && less<=0){
                    if(type==1){
                        bill.setCost(new BigDecimal(pg.getPrice().toString()));
                        totalCost=totalCost.add(new BigDecimal(pg.getPrice().toString()));
                        return true;
                    }else{
                        bill.setOffer(new BigDecimal(pg.getPrice().toString()));
                        totalOffer=totalOffer.add(new BigDecimal(pg.getPrice().toString()));
                        return true;
                    }
                }
            }
        }

        //遍历续重区间
        for(PricingGroupVo pp: Continued){

            //根据城市锁定价格计算规则
            if(bill.getDestination().startsWith(pp.getCity())){

                //和区间开始比较
                int greaterContinue=bill.getWeight().compareTo(new BigDecimal(pp.getAreaBegin().toString()));

                //和区间结束大小比较
                int lessContinue=bill.getWeight().compareTo(new BigDecimal(pp.getAreaEnd().toString()));

                int firstOne=bill.getWeight().compareTo(new BigDecimal(pp.getFirstWeight().toString()));

                if(greaterContinue>=0 && lessContinue<=0){

                    if(firstOne<=0){
                        if(type==1){
                            bill.setCost(new BigDecimal(pp.getFirstWeightPrice().toString()));
                            totalCost=totalCost.add(new BigDecimal(pp.getFirstWeightPrice().toString()));
                            return true;
                        }else{
                            bill.setOffer(new BigDecimal(pp.getFirstWeightPrice().toString()));
                            totalOffer=totalOffer.add(new BigDecimal(pp.getFirstWeightPrice().toString()));
                            return true;
                        }
                    }

                    //获取计算的单位个数
                    BigDecimal bd=bill.getWeight()
                            .subtract(new BigDecimal(pp.getFirstWeight().toString()))
                            .divide(new BigDecimal(pp.getWeightStandard().toString()))
                            .multiply(new BigDecimal(1000));

                    Integer num=bd.intValue();

                    if(num%1000!=0){
                        num=num/1000+1;
                    }else{
                        num=num/1000;
                    }

                    //获取首重的钱
                    BigDecimal fist=new BigDecimal(pp.getFirstWeightPrice().toString());

                    //计算续重的钱
                    BigDecimal two=new BigDecimal(pp.getPrice().toString()).multiply(new BigDecimal(num));

                    if(type==1){
                        bill.setCost(fist.add(two));
                        totalCost=totalCost.add(fist.add(two));
                        return true;
                    }else{
                        bill.setOffer(fist.add(two));
                        totalOffer=totalOffer.add(fist.add(two));
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 计算追加
     * @param bill
     * @param first
     * @param Continued
     * @return
     */
    public void additional(Bill bill,List<PricingGroupVo> first,List<PricingGroupVo> Continued,Integer type){
        //遍历首重
        for(PricingGroupVo pg: first){

            //根据城市锁定价格计算规则
            if(bill.getDestination().startsWith(pg.getCity())){

                //和区间开始比较
                int greater=bill.getWeight().compareTo(new BigDecimal(pg.getAreaBegin().toString()));

                //和区间结束大小比较
                int less=bill.getWeight().compareTo(new BigDecimal(pg.getAreaEnd().toString()));

                //在区间，计算首重
                if(greater>=0 && less<=0){
                    if(type==1){
                        bill.setCost(bill.getCost().add(new BigDecimal(pg.getPrice().toString())));
                        totalCost=totalCost.add(new BigDecimal(pg.getPrice().toString()));
                    }else{
                        bill.setOffer(bill.getOffer().add(new BigDecimal(pg.getPrice().toString())));
                        totalOffer=totalOffer.add(new BigDecimal(pg.getPrice().toString()));
                    }
                }
            }
        }

        //遍历续重区间
        for(PricingGroupVo pp: Continued){

            //根据城市锁定价格计算规则
            if(bill.getDestination().startsWith(pp.getCity())){

                //和区间开始比较
                int greaterContinue=bill.getWeight().compareTo(new BigDecimal(pp.getAreaBegin().toString()));

                //和区间结束大小比较
                int lessContinue=bill.getWeight().compareTo(new BigDecimal(pp.getAreaEnd().toString()));

                int firstOne=bill.getWeight().compareTo(new BigDecimal(pp.getFirstWeight().toString()));

                if(greaterContinue>=0 && lessContinue<=0){

                    if(firstOne<=0){
                        if(type==1){
                            bill.setCost(bill.getCost().add(new BigDecimal(pp.getPrice().toString())));
                            totalCost=totalCost.add(new BigDecimal(pp.getFirstWeightPrice().toString()));
                        }else{
                            bill.setOffer(bill.getOffer().add(new BigDecimal(pp.getPrice().toString())));
                            totalOffer=totalOffer.add(new BigDecimal(pp.getFirstWeightPrice().toString()));
                        }
                    }

                    //获取计算的单位个数
                    BigDecimal bd=bill.getWeight()
                            .subtract(new BigDecimal(pp.getFirstWeight().toString()))
                            .divide(new BigDecimal(pp.getWeightStandard().toString()))
                            .multiply(new BigDecimal(1000));

                    Integer num=bd.intValue();

                    if(num%1000!=0){
                        num=num/1000+1;
                    }else{
                        num=num/1000;
                    }

                    //获取首重的钱
                    BigDecimal fist=new BigDecimal(pp.getFirstWeightPrice().toString());

                    //计算续重的钱
                    BigDecimal two=new BigDecimal(pp.getPrice().toString()).multiply(new BigDecimal(num));

                    if(type==1){
                        bill.setCost(bill.getCost().add(fist.add(two)));
                        totalCost=totalCost.add(fist.add(two));
                    }else{
                        bill.setOffer(bill.getOffer().add(fist.add(two)));
                        totalOffer=totalOffer.add(fist.add(two));
                    }

                }
            }
        }
    }
}
