package com.mmall.constants;

public class LevelConstants {

    //弗恩平台
    public static final Integer SUPER = 1;
    //快递平台
    public static final Integer EXPRESS = 2;
    //快递平台分支
    public static final Integer BRANCH = -1;
    //客户平台
    public static final Integer SERVICE = 3;



    //超级管理员
    public static final Integer SUPER_MAN = 1;
    //老板号
    public static final Integer ADMIN = 2;
    //运营号
    public static final Integer OPERATE = 3;
    //客服
    public static final Integer SERVICE_PHONE = 4;
    //客户
    public static final Integer CUSTOMER = 5;

    //文件的项目路径
    public static String OMPPATH="http://www.funwl.com:8090/total/";

    //创建文件写入路径
    //public static String REALPATH = "C:\\Program Files\\apache-tomcat-9.0.12\\webapps\\total\\";
    public static String REALPATH = "/usr/local/tomcat/apache-tomcat-9.0.12/webapps/total/";

    //省计数据循环规则
    public static String[] PROSTR={"北京","天津","河北","山西","内蒙古","辽宁","吉林","黑龙江","上海","江苏","浙江","安徽","福建",
            "江西","山东","河南","湖北","湖南","广东","广西","海南","重庆","四川","贵州","云南","西藏","陕西","甘肃",
            "青海","宁夏","新疆","台湾","香港","澳门"};

    //每日单量循环规则
    public static String[] DAILY_ORIGINAL={"01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22"
            ,"23","24","25","26","27","28","29","30","31"};

    //重量区间循环规则
    public static Double[] INTERVAL={0.01,0.5,1.0,2.0,3.0,4.0,5.0,6.0,7.0,8.0,9.0,10.0};
}
