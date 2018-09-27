package com.mmall.excel.imp;

import com.google.common.collect.ArrayListMultimap;
import com.mmall.excel.Bill;
import com.mmall.model.SysUserInfo;
import java.math.BigDecimal;
import java.util.List;


/**
 * 处理Excel每一列的数据
 * @author qty
 * @since 2018-09-19
 */
public class ProcessTransDetailDataDto {

    private int readRowTitleIndex = 0; //读取标题汇总行
    public String time;//时间
    public Integer total=0;//总单量
    public BigDecimal weight=BigDecimal.ZERO;//总重

    //根据店铺分离数据
    public ArrayListMultimap<String, Bill> map = ArrayListMultimap.create();

    /**
     * Excel每一列数据的处理
     * @param rowStrs
     * @param currentRowNumber
     */
    public void processTransTotalData(String rowStrs, int currentRowNumber) {

        String[] cellStrs = rowStrs.split("\\|@\\|");


        // 读取第二行汇总行
        if (currentRowNumber != readRowTitleIndex) {

            String nameStr=cellStrs[0];


            //分表--为相应表格添加数据
            Bill bill=new Bill();
            bill.setBillName(cellStrs[0]);
            bill.setSweepTime(cellStrs[1]);
            bill.setSerialNumber(cellStrs[2]);
            bill.setDestination(cellStrs[3]);
            bill.setWeight(new BigDecimal(cellStrs[4]));

            //计算每个月份的单量，总重量
            total+=1;
            weight=weight.add(new BigDecimal(cellStrs[4]));


            //目的地是否合法
            boolean province = province(cellStrs[3]);
            if(!province){
                nameStr="未识别地址账单";
            }

            map.put(nameStr,bill);

        }
    }


    /**
     * 获取城市单件：http://www.tcmap.com.cn/list/jiancheng_list.html
     * @param province
     */
    public boolean province(String province){

        boolean a=false;

        String[] proStr={"北京","天津","河北","山西","内蒙古","辽宁","吉林","黑龙江","上海","江苏","浙江","安徽","福建",
                "江西","山东","河南","湖北","湖南","广东","广西","海南","重庆","四川","贵州","云南","西藏","陕西","甘肃",
                "青海","宁夏","新疆","台湾","香港","澳门"};

        for (String str:proStr){
            if(province.startsWith(str)){
                a=true;
                break;
            }
        }
        return a;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
