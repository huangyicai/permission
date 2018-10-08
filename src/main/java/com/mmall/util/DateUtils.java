package com.mmall.util;

import org.apache.shiro.crypto.hash.Md5Hash;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类 by hpf
 * */
public class DateUtils{

    /**
     * 判断闰年
     * @param year
     * @return
     */
    public static boolean isLeap(int year)
    {
        if (((year % 100 == 0) && year % 400 == 0) || ((year % 100 != 0) && year % 4 == 0))
            return true;
        else
            return false;
    }

    /**
     * 返回当月天数
     * @param date
     * @return
     */
    public static int getDays(String date){

        Integer year=0;
        Integer month=0;

        month=Integer.parseInt(date.split("-")[1]);
        year=Integer.parseInt(date.split("-")[0]);

        int days;
        int FebDay = 28;
        if (isLeap(year))
            FebDay = 29;
        switch (month)
        {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                days = 31;
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                days = 30;
                break;
            case 2:
                days = FebDay;
                break;
            default:
                days = 0;
                break;
        }
        return days;
    }
}

