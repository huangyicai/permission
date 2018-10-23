package com.mmall.util;

import org.apache.shiro.crypto.hash.Md5Hash;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 日期工具类
 * */
public class DateUtils{

    public static final String FORMATYYYYMMMDD = "yyyy-MM-dd||yyyy.MM.dd||yyyy/MM/dd";
    public static final String FORMATYYYYMMDD_Z = "yyyy年MM月dd日";
    public static final String FORMATYYYYMMDDHHMMSS = "yyyy.MM.dd HH:mm:ss||yyyy/MM/dd HH:mm:ss||yyyy-MM-dd HH:mm:ss";
    public static final String FORMATYYYYMMDDHHMMSS_Z = "yyyy年MM月dd日 HH时mm分ss秒";
    public static final String FORMATUNCR = "格式不符合要求";

    public static Date convertStringtoDate(String time) throws Exception{
        Date date = null;
        String format = judgeFormat(time);
        if(FORMATYYYYMMMDD.equals(format)||FORMATYYYYMMDD_Z.equals(format)){
            time = convertString(format,time);
            date = new SimpleDateFormat("yyyy-MM-dd").parse(time);
            return date;
        }else if(FORMATYYYYMMDDHHMMSS.equals(format)||FORMATYYYYMMDDHHMMSS_Z.equals(format)){
            time = convertString(format,time);
            date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(time);
            return date;
        }
        throw new Exception(FORMATUNCR);
    }

    public static String judgeFormat(String time){
//		String regex1 = "^\\d{4}\\S{1}\\d{2}\\S{1}\\d{2}$";
//		String regex2 = "^\\d{4}\\S{1}\\d{2}\\S{1}\\d{2}\\s+\\d{2}:\\d{2}:\\d{2}$";
        String regex1 = "^\\s*\\d{4}(-|/|\\.){1}\\d{2}(-|/|\\.){1}\\d{2}\\s*";
        String regex2 = "^\\s*\\d{4}(-|/|\\.){1}\\d{2}(-|/|\\.){1}\\d{2}\\s+(\\d{2}:){2}\\d{2}\\s*";
        String regex3 = "^\\s*\\d{4}([\u4e00-\u9fa5]){1}\\d{2}([\u4e00-\u9fa5]){1}\\d{2}([\u4e00-\u9fa5]){1}\\s*";
        String regex4 = "^\\s*\\d{4}([\u4e00-\u9fa5]){1}(\\d{2}([\u4e00-\u9fa5]){1}){2}\\s*(\\d{2}([\u4e00-\u9fa5]){1}){3}\\s*";
        Pattern p1 = Pattern.compile(regex1);
        Matcher m1 = p1.matcher(time);
        if(m1.matches()){
            return FORMATYYYYMMMDD;
        }
        Pattern p2 = Pattern.compile(regex2);
        Matcher m2 = p2.matcher(time);
        if(m2.matches()){
            return FORMATYYYYMMDDHHMMSS;
        }
        Pattern p3 = Pattern.compile(regex3);
        Matcher m3 = p3.matcher(time);
        if(m3.matches()){
            return FORMATYYYYMMDD_Z;
        }
        Pattern p4 = Pattern.compile(regex4);
        Matcher m4 = p4.matcher(time);
        if(m4.matches()){
            return FORMATYYYYMMDDHHMMSS_Z;
        }
        return FORMATUNCR;
    }

    public static String convertString(String format, String time){
        if(FORMATYYYYMMMDD.equals(format)||FORMATYYYYMMDDHHMMSS.equals(format)){
            time = time.replaceAll("/|\\.", "-");
        }else if(FORMATYYYYMMDD_Z.equals(format)){
            time = time.replaceAll("[\u4e00-\u9fa5]", "-");
            time = time.substring(0, time.length() - 1);
        }else if(FORMATYYYYMMDDHHMMSS_Z.equals(format)){
            time = time.replaceAll("[\u4e00-\u9fa5]", "-");
            String[] timeArray = time.split("-");
            time = timeArray[0] + "-" + timeArray[1] + "-" + timeArray[2] + " " + timeArray[3] + ":" + timeArray[4] +":"+ timeArray[5];
        }
        return time;
    }


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

