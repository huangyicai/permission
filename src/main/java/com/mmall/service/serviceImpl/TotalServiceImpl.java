package com.mmall.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.mmall.config.UserInfoConfig;
import com.mmall.dao.PricingGroupMapper;
import com.mmall.dao.TotalMapper;
import com.mmall.dto.BillDto;
import com.mmall.dto.ProfitsDto;
import com.mmall.excel.Bill;
import com.mmall.excel.export.DataSheetExecute;
import com.mmall.excel.export.ExcelExportExecutor;
import com.mmall.excel.imp.XlsxProcessAbstract;
import com.mmall.model.PricingGroup;
import com.mmall.model.Response.InfoEnums;
import com.mmall.model.Response.Result;
import com.mmall.model.SysUserInfo;
import com.mmall.model.Total;
import com.mmall.model.params.BillDetailsParam;
import com.mmall.model.params.BillParam;
import com.mmall.model.params.TotalIncomeParam;
import com.mmall.service.PricingGroupService;
import com.mmall.service.SysUserInfoService;
import com.mmall.service.TotalService;
import com.mmall.util.DateUtils;
import com.mmall.util.LevelUtil;
import com.mmall.util.RandomHelper;
import com.mmall.util.UploadApi;
import com.mmall.vo.PricingGroupVo;
import com.mmall.vo.TotalVo;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
    private  XlsxProcessAbstract xlsxProcessAbstract;

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
            billParam.setUserId(getUserIdStr());
        }

        Total one = totalMapper.getToal(billParam.getDate(), billParam.getUserId());

        if(one==null){
            return null;
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
    public IPage<TotalVo> getBill(IPage<TotalVo> page, BillDetailsParam billDetailsParam) {
        String nameStr=getUserIdStr();

        if(!"".equals(billDetailsParam.getUserId()) && billDetailsParam.getUserId()!=null){
            nameStr=billDetailsParam.getUserId().toString();
        }

        Page<TotalVo> bill = totalMapper.getBill(page,billDetailsParam.getDate(),nameStr, billDetailsParam.getState());
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
            billParam.setUserId(getUserIdStr());
        }

        Total one = totalMapper.getToal( billParam.getDate(), billParam.getUserId());

        if(one==null){
            return null;
        }

        ProfitsDto billDto=new ProfitsDto();
        billDto.setTotalNumber(one.getTotalNumber());
        billDto.setTotalWeight(one.getTotalWeight());
        billDto.setAverageWeight(one.getTotalWeight().divide(new BigDecimal(one.getTotalNumber()),2, RoundingMode.DOWN));
        billDto.setTotalOffer(one.getTotalOffer());
        billDto.setTotalPaid(one.getTotalPaid());
        billDto.setTotalCost(one.getTotalCost());
        billDto.setProfits(one.getTotalPaid().subtract(one.getTotalCost()));
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
    public Result<String> getPricing(Integer totalId) {

        //获取账单信息
        Total total = totalMapper.selectById(totalId);

        if(total==null){
            return  Result.error(InfoEnums.BILL_IS_NULL);
        }

        //獲取报价表
        List<PricingGroupVo> pricingGroup = pricingGroupMapper.ListPricingGroup(total.getUserId());

        //获取成本表
        SysUserInfo user = (SysUserInfo) SecurityUtils.getSubject().getSession().getAttribute("user");
        List<PricingGroupVo> pricingOffer = pricingGroupMapper.ListPricingGroup(user.getUserId());

        //定价表是否数据存在
        List<Integer> allPricingGroups = pricingGroupMapper.getAllPricingGroups(total.getUserId());
        if(allPricingGroups.size()!=34){
            return Result.error(InfoEnums.PROCING_IS_NULL);
        }

        //校验成本表是否存在数据
        List<Integer> ag = pricingGroupMapper.getAllPricingGroups(user.getId());
        if(ag.size()!=34){
            return Result.error(InfoEnums.COST_IS_NULL);
        }

        String[] str=total.getTotalUrl().split("/");
//        final String ompPath=path+str[str.length-1];

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

        //计算成本
        list= getCalculate(pricingOffer,map,1,list);

        //计算报价
        list=getCalculate(pricingGroup,map,2,list);

        //写入Excel
        String[] strings = {"商家名称", "扫描时间", "运单编号", "目的地", "快递重量","成本","报价"};

        DataSheetExecute<Bill> dataSheetExecute = new DataSheetExecute<Bill>() {

            public void execute(Row row, Bill personUser) {
                row.createCell(0).setCellValue(personUser.getBillName());
                row.createCell(1).setCellValue(personUser.getSweepTime());
                row.createCell(2).setCellValue(personUser.getSerialNumber());
                row.createCell(3).setCellValue(personUser.getDestination());
                row.createCell(4).setCellValue(personUser.getWeight().toString());
                row.createCell(5).setCellValue(personUser.getCost().toString());
                row.createCell(6).setCellValue(personUser.getOffer().toString());
            }

            public void writeExcel(SXSSFWorkbook workbook, OutputStream outputStream) throws Exception {
                outputStream = new FileOutputStream(total.getCdUrl());
                workbook.write(outputStream);
                outputStream.close();
                workbook.close();
            }

            public void listen(Row row, int rows) {
//                System.out.println("执行到了：<" + rows + "> 这一行");
            }
        };

        new ExcelExportExecutor<Bill>(strings, list, dataSheetExecute, true).execute();

        //上传到oss
        File file=new File(total.getCdUrl());
        String upload = UploadApi.upload(file, str[str.length-1], total.getTotalTime()+"/"+total.getName()+"/");

        total.setTotalCost(totalCost);
        total.setTotalOffer(totalOffer);
        total.setTotalUrl(upload);
        total.setUpdateTime(new Date());

        totalMapper.updateById(total);
        return Result.ok(upload);
    }

    @Override
    public Result polling(String time, Integer id) {
        List<Total> totals = totalMapper.getTotals(time, id);
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

        nameStr=nameStr.substring(0,nameStr.length()-1);
        return nameStr;
    }

    /**
     * 计算价格
     * @param pricingGroupVo 计算表
     * @param map 账单数据
     * @param type  计算类型
     * @param list  计算后的数据
     * @return
     */
    public List<Bill> getCalculate(List<PricingGroupVo> pricingGroupVo,
                                   ArrayListMultimap<String, Bill> map,
                                   Integer type,
                                   List<Bill> list){
        //确定遍历的账单集合
        if(type==1){
            for (String key:map.keySet()) {
                list=map.get(key);
            }
        }

        //首重集合
        List<PricingGroupVo> first=new ArrayList<PricingGroupVo>();

        //续重集合
        List<PricingGroupVo> Continued=new ArrayList<PricingGroupVo>();

        //分离首重和续重
        for(PricingGroupVo p:pricingGroupVo){

            //首重
            if(p.getFirstOrContinued()==1){
                first.add(p);
                continue;
            }

            if(p.getFirstOrContinued()==2){
                Continued.add(p);
                continue;
            }
        }


        //获取每一条账单数据
        for (Bill bill:list) {

            int i=1;

            //遍历首重
            for(PricingGroupVo pg: first){

                //根据城市锁定价格计算规则
                if(pg.getCity().startsWith(bill.getDestination())){

                    //和区间开始比较
                    int greater=bill.getWeight().compareTo(new BigDecimal(pg.getAreaBegin()));

                    //和区间结束大小比较
                    int less=bill.getWeight().compareTo(new BigDecimal(pg.getAreaEnd()));
                    i++;

                    //在区间，计算首重
                    if(greater>=0 && less<=0){
                        if(type==1){
                            bill.setCost(new BigDecimal(pg.getPrice()));
                            totalCost=totalCost.add(new BigDecimal(pg.getPrice()));
                        }else{
                            bill.setOffer(new BigDecimal(pg.getPrice()));
                            totalOffer=totalOffer.add(new BigDecimal(pg.getPrice()));
                        }
                        break;
                    }

                    //不在区间，计算续重
                    if(i==first.size() && less==1){

                        //遍历续重区间
                        for(PricingGroupVo pp: Continued){

                            //和区间开始比较
                            int greaterContinue=bill.getWeight().compareTo(new BigDecimal(pp.getAreaBegin()));

                            //和区间结束大小比较
                            int lessContinue=bill.getWeight().compareTo(new BigDecimal(pp.getAreaEnd()));

                            if(greaterContinue>=0 && lessContinue<=0){

                                //获取计算的单位个数
                                BigDecimal bd=bill.getWeight()
                                        .subtract(new BigDecimal(first.get(first.size()-1).getAreaBegin()))
                                        .divide(new BigDecimal(pp.getWeightStandard()))
                                        .multiply(new BigDecimal(100));

                                Integer num=bd.intValue();

                                if(num%100!=0){
                                    num=num/100+1;
                                }else{
                                    num=num/100;
                                }

                                //获取首重的钱
                                BigDecimal fist=new BigDecimal(pp.getFirstWeightPrice());

                                //计算续重的钱
                                BigDecimal two=new BigDecimal(pp.getCity()).multiply(new BigDecimal(num));

                                if(type==1){
                                    bill.setCost(fist.add(two));
                                    totalCost=totalCost.add(fist.add(two));
                                }else{
                                    bill.setOffer(fist.add(two));
                                    totalOffer=totalOffer.add(fist.add(two));
                                }
                                break;
                            }
                        }
                    }
                }
            }
        }


        return list;
    }

}
