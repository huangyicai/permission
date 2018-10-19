package com.mmall.excel.imp;

import com.google.common.collect.ArrayListMultimap;
import com.mmall.excel.Bill;
import com.mmall.model.SysUserInfo;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


/**
 * 处理Excel每一列的数据
 * @author qty
 * @since 2018-09-19
 */
public class ProcessTransDetailDataDto {

    private int readRowTitleIndex = 0; //读取标题汇总行

    public String time;//时间


    //根据店铺分离数据
    public ArrayListMultimap<String, Bill> map = ArrayListMultimap.create();

    //获取整个数据集合
//    public List<Bill> bills=new ArrayList<>();

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
            map.put(nameStr,bill);
//            bills.add(bill);
        }
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
