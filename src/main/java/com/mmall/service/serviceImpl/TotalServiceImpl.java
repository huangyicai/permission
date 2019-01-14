package com.mmall.service.serviceImpl;

import com.aliyuncs.exceptions.ClientException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.mmall.Socket.ExpressWebSocket;
import com.mmall.config.UserInfoConfig;
import com.mmall.constants.LevelConstants;
import com.mmall.dao.PricingGroupMapper;
import com.mmall.dao.SpecialPricingGroupMapper;
import com.mmall.dao.SysUserInfoMapper;
import com.mmall.dao.TotalMapper;
import com.mmall.dto.BillDto;
import com.mmall.dto.ProfitsDto;
import com.mmall.excel.Bill;
import com.mmall.excel.export.DataSheetExecute;
import com.mmall.excel.export.ExcelExportExecutor;
import com.mmall.excel.imp.XlsxProcessAbstract;
import com.mmall.excel.thread.PricingThread;
import com.mmall.model.*;
import com.mmall.model.Response.InfoEnums;
import com.mmall.model.Response.Result;
import com.mmall.model.params.BillDetailsParam;
import com.mmall.model.params.BillParam;
import com.mmall.model.params.TotalIncomeParam;
import com.mmall.model.params.TotalParam;
import com.mmall.service.*;
import com.mmall.util.DateUtils;
import com.mmall.util.LevelUtil;
import com.mmall.util.SmsUtil;
import com.mmall.util.UploadApi;
import com.mmall.vo.PricingGroupVo;
import com.mmall.vo.TotalVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.math.BigDecimal.ROUND_HALF_UP;

/**
 * <p>
 * 月计表(客户账单) 服务实现类
 * </p>
 *
 * @author qty
 * @since 2018-09-25
 */
@Service
@Slf4j
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

    @Autowired
    private SysUserInfoMapper sysUserInfoMapper;

    @Autowired
    private PrepaidService prepaidService;


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

        Total one = totalMapper.getToal(billParam.getDate(),billParam.getEndDate(), billParam.getUserId(),userInfo.getId(),"2,3,4,5");

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
        String nameStr=null;
        if(!"".equals(billDetailsParam.getUserId()) && billDetailsParam.getUserId()!=null){
            if(billDetailsParam.getUserId()==-1){
                nameStr=billDetailsParam.getUserId().toString();
            }else{
                SysUserInfo sysUser = sysUserInfoMapper.selectById(billDetailsParam.getUserId());
                if(sysUser.getPlatformId()==-1){
                    nameStr = getUserIdStrBatch(sysUser);
                }else{
                    nameStr=billDetailsParam.getUserId().toString();
                }
            }
        }else{
            nameStr = getUserIdStrBatch(userInfo);
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

        //应收数据
        Total Offer = totalMapper.getToal( billParam.getDate(),billParam.getEndDate(), billParam.getUserId(),userInfo.getId(),"2,3,4,5");

        //实收数据
        Total Paid = totalMapper.getToal( billParam.getDate(),billParam.getEndDate(), billParam.getUserId(),userInfo.getId(),"4");
        if(Offer==null){
            Offer = check(new Total());
        }if(Paid==null){
            Paid=check(new Total());
        }

        ProfitsDto billDto=new ProfitsDto();

        //计算总单量
        billDto.setTotalNumber(Offer.getTotalNumber());

        //计算每日单量
        int days = DateUtils.getDays(billParam.getDate());
        billDto.setAverageNumber(billDto.getTotalNumber()/days);

        //计算总总量
        billDto.setTotalWeight(Offer.getTotalWeight());

        //计算平均重量
        if (new BigDecimal(billDto.getTotalNumber()).compareTo(new BigDecimal(0)) == 0) {
            billDto.setAverageWeight(new BigDecimal(0));
        } else {
            billDto.setAverageWeight(billDto.getTotalWeight().divide(new BigDecimal(billDto.getTotalNumber()), 2, RoundingMode.DOWN));
        }


        //获取应收
        billDto.setTotalOffer(Offer.getTotalOffer().add(Offer.getTotalAdditional()));

        //计算应收成本
        billDto.setOfferCost(Offer.getTotalCost());

        //计算应收利润
        billDto.setOfferProfits(billDto.getTotalOffer().subtract(billDto.getOfferCost()));

        //计算应收单价,和应收的成本单价
        if(Offer.getTotalNumber()==0){
            billDto.setOfferOnePrice(new BigDecimal(0));
            billDto.setOfferCostOne(new BigDecimal(0));
        }else{
            billDto.setOfferOnePrice(Offer.getTotalOffer().divide(new BigDecimal(Offer.getTotalNumber()), 2, RoundingMode.DOWN));
            billDto.setOfferCostOne(Offer.getTotalCost().divide(new BigDecimal(Offer.getTotalNumber()), 2, RoundingMode.DOWN));
        }

        //计算应收单间利润
        billDto.setOfferOneProfits(billDto.getOfferOnePrice().subtract(billDto.getOfferCostOne()));

        //应收利润百分比
        if(billDto.getTotalOffer().compareTo(new BigDecimal(0))==0){
            billDto.setOfferProfitsPercentage(new BigDecimal(0));
        }else{
            billDto.setOfferProfitsPercentage(billDto.getOfferProfits().divide(billDto.getTotalOffer(), 4, RoundingMode.DOWN));

        }

        //获取实收
        billDto.setTotalPaid(Paid.getTotalPaid());

        //计算实收成本
        billDto.setPaidCost(Paid.getTotalCost());

        //计算实收利润
        billDto.setPaidProfits(billDto.getTotalPaid().subtract(billDto.getPaidCost()));

        //计算实收单价,和实收成本单价
        if(Paid.getTotalNumber()==0){
            billDto.setPaidOnePrice(new BigDecimal(0));
            billDto.setPaidCostOne(new BigDecimal(0));
        }else{
            billDto.setPaidOnePrice(Paid.getTotalPaid().divide(new BigDecimal(Paid.getTotalNumber()), 2, RoundingMode.DOWN));
            billDto.setPaidCostOne(Paid.getTotalCost().divide(new BigDecimal(Paid.getTotalNumber()), 2, RoundingMode.DOWN));
        }

        //计算实收单间利润
        billDto.setPaidOneProfits(billDto.getPaidOnePrice().subtract(billDto.getPaidCostOne()));

        //实收利润百分比
        if((billDto.getTotalPaid().compareTo(new BigDecimal(0))==0)){
            billDto.setPaidProfitsPercentage(new BigDecimal(0));
        }else{
            billDto.setPaidProfitsPercentage(billDto.getPaidProfits().divide(billDto.getTotalPaid(),4, RoundingMode.DOWN));
        }
        return billDto;
    }

    //利用反射赋值
    public Total check(Total total){
        total.setTotalNumber(0);
        total.setTotalWeight(new BigDecimal(0));
        total.setTotalCost(new BigDecimal(0));
        total.setTotalOffer(new BigDecimal(0));
        total.setTotalPaid(new BigDecimal(0));
        total.setTotalAdditional(new BigDecimal(0));
        return total;
    }

    /**
     * 定价
     * @param totalId
     * @return
     */
    @Transactional
    public Result getPricing(Integer totalId) {

        //成本
        BigDecimal totalCost=BigDecimal.ZERO;

        //报价
        BigDecimal totalOffer=BigDecimal.ZERO;

        //获取账单信息
        Total total = totalMapper.selectById(totalId);

        if(total==null){
            return  Result.error(InfoEnums.BILL_IS_NULL);
        }

        //判断是否能够定价
        if(total.getTotalState()>1){
            return Result.error(InfoEnums.TOATL_IS_PRICING);
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

        //判断是否成本定价
        SysUserInfo userInfo = UserInfoConfig.getUserInfo();
        SysUserInfo byId = sysUserInfoService.getById(userInfo.getId());
        if(byId.getPricingStatus()==1){

            //获取成本表
            List<PricingGroupVo> pricingOffer = pricingGroupMapper.ListPricingGroup(total.getSendId());

            //获取特殊成本表
            List<PricingGroupVo> special1 = specialPricingGroupMapper.getPricingGroupVo(total.getSendId());

            //计算成本
            list= getCalculate(pricingOffer,1,list,special1);

        }

        //计算报价
        list=getCalculate(pricingGroup,2,list,special);

        //计算价格
        for(Bill b:list){
            totalCost=totalCost.add(b.getCost());
            totalOffer=totalOffer.add(b.getOffer());
        }

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

        //计算预付
        Prepaid by = prepaidService.getOne(new QueryWrapper<Prepaid>().eq("user_id",total.getUserId()));
        BigDecimal money=BigDecimal.ZERO;
        if(by!=null && by.getMoney().compareTo(new BigDecimal("0"))>0){
            money=by.getMoney().multiply(new BigDecimal(list.size()+""));
        }

        Total total1=totalMapper.selectById(totalId);
        total1.setTotalCost(totalCost);
        total1.setTotalOffer(totalOffer);
        total1.setTotalPaid(money);
        total1.setTotalState(1);
        total1.setTotalUrl(upload);
        total1.setUpdateTime(new Date());

        totalMapper.updateById(total1);
        map.clear();
        return Result.ok(total1);
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
                .in("status",0,1)
                .select("id"));

        String nameStr="";
        for(SysUserInfo sysUserInfo: list1){
            nameStr+=sysUserInfo.getId()+",";
        }

        nameStr=nameStr.substring(0,nameStr.length()!=0?nameStr.length()-1:0);
        return nameStr;
    }

    public String getUserIdStrBatch(SysUserInfo user){
        //判断是否是客户
        if(user.getPlatformId()==3){
            return user.getId().toString();
        }
        String s = LevelUtil.calculateLevel(user.getLevel(), user.getId());
        List<SysUserInfo> list1 = sysUserInfoService.list(new QueryWrapper<SysUserInfo>()
                .like("level", s)
                .eq("platform_id", 3)
                .in("status",0,1)
                .select("id"));

        String nameStr="";
        for(SysUserInfo sysUserInfo: list1){
            nameStr+=sysUserInfo.getId()+",";
        }

        nameStr=nameStr.substring(0,nameStr.length()!=0?nameStr.length()-1:0);
        return nameStr;
    }



    @Override
    public Result getBillDetails(Integer status,SysUserInfo userInfo, String userId,
                                 String date,String endDate, Page ipage) {
        Total sumBiLLDetails = totalMapper.getSumBiLLDetails( status,userId, date,endDate, userInfo.getId());
        totalMapper.getAllBySendIdAndCreateTimeAndUserIds(ipage,status,userId,date,endDate,userInfo.getId());
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
    public Result deleteTotal(String totalId) {
        String[] i = totalId.split(",");
        boolean totalId1 = totalService.removeByIds(Arrays.asList(i));
         //= totalService.remove(new UpdateWrapper<Total>().eq("total_id", totalId));
        if(totalId1){
            weightCalculateService.removeByIds(Arrays.asList(i));
            provinceCalculateService.removeByIds(Arrays.asList(i));
            dailyTotalService.removeByIds(Arrays.asList(i));
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

//        //定价表是否数据存在
//        List<Integer> allPricingGroups = pricingGroupMapper.getAllPricingGroups(userId);

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
     * 批量发送订单
     * @param totalParam
     * @return
     */
    @Override
    @Transactional
    public Result sendAll(TotalParam totalParam) {
        String totalId = totalParam.getTotalId();
        List<Total> allBillByIds = totalMapper.getAllBillByIds(totalId);
        for(Total tt:allBillByIds){
            if(tt.getTotalState()!=1){
                return Result.error(InfoEnums.SEND_FAILURE,"单号："+tt.getOrderNo()+"，请检查是否定价，或者已经发送");
            }
            TotalParam totalParam1=new TotalParam();
            totalParam1.setTotalId(tt.getTotalId().toString());
            totalParam1.setDate(totalParam.getDate());
            this.sendSms(totalParam1);
        }
        totalMapper.updateByTotalId(totalId,totalParam.getTotalRemark(),totalParam.getDate(),new BigDecimal(totalParam.getTotalAdditional()));
        SysUserInfo sysUserInfo = sysUserInfoMapper.selectById(allBillByIds.get(0).getUserId());
        ExpressWebSocket.sendMsgTotals(sysUserInfo,allBillByIds);
        return Result.ok();
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

            //遍历取代定价组
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
    public Boolean traverse(Bill bill,List<PricingGroupVo> first,
                            List<PricingGroupVo> Continued,
                            Integer type){

        //遍历首重
        for(PricingGroupVo pg: first){

            //根据城市锁定价格计算规则
            if(bill.getDestination().contains(pg.getCity())){

                //和区间开始比较
                int greater=bill.getWeight().compareTo(new BigDecimal(pg.getAreaBegin().toString()));

                //和区间结束大小比较
                int less=bill.getWeight().compareTo(new BigDecimal(pg.getAreaEnd().toString()));

                //在区间，计算首重
                if(greater>=0 && less<=0){
                    if(type==1){
                        bill.setCost(new BigDecimal(pg.getPrice().toString()).setScale(2));
                        return true;
                    }else{
                        bill.setOffer(new BigDecimal(pg.getPrice().toString()).setScale(2));
                        return true;
                    }
                }
            }
        }

        //遍历续重区间
        for(PricingGroupVo pp: Continued){

            //根据城市锁定价格计算规则
            if(bill.getDestination().contains(pp.getCity())){

                //和区间开始比较
                int greaterContinue=bill.getWeight().compareTo(new BigDecimal(pp.getAreaBegin().toString()).setScale(2));

                //和区间结束大小比较
                int lessContinue=bill.getWeight().compareTo(new BigDecimal(pp.getAreaEnd().toString()).setScale(2));

                int firstOne=bill.getWeight().compareTo(new BigDecimal(pp.getFirstWeight().toString()).setScale(2));

                if(greaterContinue>=0 && lessContinue<=0){

                    if(firstOne<=0){
                        if(type==1){
                            bill.setCost(new BigDecimal(pp.getFirstWeightPrice().toString()).setScale(2));
                            return true;
                        }else{
                            bill.setOffer(new BigDecimal(pp.getFirstWeightPrice().toString()).setScale(2));
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
                        bill.setCost(fist.add(two).setScale(2));
                        return true;
                    }else{
                        bill.setOffer(fist.add(two).setScale(2,ROUND_HALF_UP));
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
    public void additional(Bill bill,List<PricingGroupVo> first,
                           List<PricingGroupVo> Continued,
                           Integer type){
        //遍历首重
        for(PricingGroupVo pg: first){

            //根据城市锁定价格计算规则
            if(bill.getDestination().contains(pg.getCity())||pg.getCity().startsWith("全部")){

                //和区间开始比较
                int greater=bill.getWeight().compareTo(new BigDecimal(pg.getAreaBegin().toString()).setScale(2,ROUND_HALF_UP));

                //和区间结束大小比较
                int less=bill.getWeight().compareTo(new BigDecimal(pg.getAreaEnd().toString()).setScale(2,ROUND_HALF_UP));

                //在区间，计算首重
                if(greater>=0 && less<=0){
                    if(type==1){
                        bill.setCost(bill.getCost().add(new BigDecimal(pg.getPrice().toString())).setScale(2,ROUND_HALF_UP));
                    }else{
                        bill.setOffer(bill.getOffer().add(new BigDecimal(pg.getPrice().toString())).setScale(2,ROUND_HALF_UP));
                    }
                }
            }
        }

        //遍历续重区间
        for(PricingGroupVo pp: Continued){

            //根据城市锁定价格计算规则
            if(bill.getDestination().contains(pp.getCity())||pp.getCity().startsWith("全部")){

                //和区间开始比较
                int greaterContinue=bill.getWeight().compareTo(new BigDecimal(pp.getAreaBegin().toString()));

                //和区间结束大小比较
                int lessContinue=bill.getWeight().compareTo(new BigDecimal(pp.getAreaEnd().toString()));

                int firstOne=bill.getWeight().compareTo(new BigDecimal(pp.getFirstWeight().toString()));

                if(greaterContinue>=0 && lessContinue<=0){

                    if(firstOne<=0){
                        if(type==1){
                            bill.setCost(bill.getCost().add(new BigDecimal(pp.getPrice().toString())).setScale(2,ROUND_HALF_UP));
                        }else{
                            bill.setOffer(bill.getOffer().add(new BigDecimal(pp.getPrice().toString())).setScale(2,ROUND_HALF_UP));
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
                        bill.setCost(bill.getCost().add(fist.add(two)).setScale(2,ROUND_HALF_UP));
                    }else{
                        bill.setOffer(bill.getOffer().add(fist.add(two)).setScale(2,ROUND_HALF_UP));
                    }
                }
            }
        }
    }

    /**
     * 获取未付款账单提示
     * @return
     */
    @Override
    public Result getNotPaying() {
        SysUserInfo user = (SysUserInfo) SecurityUtils.getSubject().getSession().getAttribute("user");
        List<Total> list = totalService.list(new QueryWrapper<Total>()
                .eq(user.getId() != null, "user_id", user.getId()).in("total_state",2,5));
        List<Map<String,String>> li=new ArrayList<>();

        if(list.size()==0){
            return Result.ok(li);
        }

        for(Total t:list){
            Map<String,String> map=new HashMap<>();
            map.put("totalId",t.getTotalId().toString());
            map.put("totalName",t.getName());
            map.put("totalTime",t.getTotalTime());
            map.put("money",t.getTotalOffer().subtract(t.getTotalPaid()).toString());
            map.put("state",t.getTotalState().toString());
            li.add(map);
        }
        return Result.ok(li);
    }

    /**
     * 一键定价
     * @param totalId
     * @return
     */
    @Override
    public Result keyPricing(String totalId) throws InterruptedException {

        List<Result> list=new ArrayList<>();

        if(!"".equals(totalId) && totalId!=null){
            //创建线程池
            ExecutorService threadPool = Executors.newFixedThreadPool(3);
            String[] split = totalId.split(",");
            for(String id:split){
                PricingThread pt=new PricingThread(Integer.parseInt(id),list);
                threadPool.submit(pt);
            }

            //判断线程是否执行完毕
            threadPool.shutdown();
            while (true){
                if (threadPool.isTerminated()) {
                    log.info("线程已执行完毕");
                    break;
                }
                Thread.sleep(200);
            }
        }else{
            return Result.error(InfoEnums.BILL_IS_NULL);
        }

        return Result.ok(list);
    }

    /**
     * 发送短信
     */
    public void sendSms(TotalParam totalParam){

        //用户名
        String username="";

        //用户手机
        String phone="";

        //发送人公司
        String company="";

        //账单名字
        String billName="";

        //联系人
        String name="";

        //截止时间
        String time="";

        //联系人电话
        String phoneServer="";

        //获取账单信息
        Total one = totalService.getById(totalParam.getTotalId());
        billName=one.getName();
        time=totalParam.getDate();

        //获取客户信息
        SysUserInfo user = sysUserInfoService.getById(one.getUserId());
        username=user.getName();
        phone=user.getTelephone();

        //获取发送者信息
        SysUserInfo user1 = (SysUserInfo) SecurityUtils.getSubject().getSession().getAttribute("user");
        name= user1.getPersonInCharge();
        phoneServer=user1.getTelephone();
        company=user1.getCompanyName();

        try {
            SmsUtil.sendSmsBill(phone,username,company,billName,name,phoneServer,time);
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }

}
