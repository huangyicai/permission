package com.mmall.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Huang YiCai
 * @create 2018/10/31  10:50
 */
public class DateTimeUtil {
    /**
     * 时间戳转化为时间格式
     * @param time 时间戳
     * @param format 格式
     * @return
     */
    public static String numToDate(long time,String format){
        SimpleDateFormat formatter = new SimpleDateFormat(format);//格式化为2018-10-01
        String stardtr = formatter.format(time);//使用格式化Data
        return stardtr;
    }


    /**
     * 时间格式转化为时间戳
     * @param time 时间格式
     * @return
     */
    public static long DateToNum(String time){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");//格式化为2017-10
        Date starDate = null;//得到时间赋给Data
        try {
            starDate = formatter.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return starDate.getTime();
    }

    /**
     * 时间格式增加月数
     * @param time 时间格式
     * @param methodNum  增加的月数
     * @return
     */
    public static long addDate(String time,int methodNum){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");//格式化为2017-10
        Calendar calendar = Calendar.getInstance();//得到Calendar实例

        Date starDate = null;//得到时间赋给Data
        try {
            starDate = formatter.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.setTime(starDate);
        calendar.add(Calendar.MONTH, methodNum);//把月份减三个月
        return calendar.getTime().getTime();
    }

    /**
     * 时间戳增加月数
     * @param time 时间戳
     * @param methodNum  增加的月数
     * @return
     */
    public static long addDateNum(long time,int methodNum){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");//格式化为2017-10
        Calendar calendar = Calendar.getInstance();//得到Calendar实例
        Date starDate = null;//得到时间赋给Data
        try {
            starDate = formatter.parse(formatter.format(time));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.setTime(starDate);
        calendar.add(Calendar.MONTH, methodNum);
        return calendar.getTime().getTime();
    }

}
