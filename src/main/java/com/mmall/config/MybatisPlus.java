package com.mmall.config;


import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.po.TableFill;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.config.rules.PropertyInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author Huang YiCai
 * @create 2018/09/15  15:28
 */

public class MybatisPlus {


    //文件路径
    private static String packageName="";

    //作者
    private static String authorName="hyc";

    //table名字
    private static String table="payment_method";

    //table前缀
    private static String prefix="";

    private static File file = new File(packageName);
        private static String path = "E:\\java\\manage\\permission";
    //private static String path = "E:\\java\\manage";//--qyy
    private static String pathTwo = "\\src\\main\\java\\com\\mmall";
    public static void main(String[] args) {
        // 自定义需要填充的字段
        List<TableFill> tableFillList = new ArrayList<TableFill>();
        tableFillList.add(new TableFill("ASDD_SS", FieldFill.INSERT_UPDATE));
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();
        mpg.setGlobalConfig(
                // 全局配置
                new GlobalConfig()
                        .setOutputDir(path + "/src")//输出目录
                        .setFileOverride(true)// 是否覆盖文件
                        .setActiveRecord(true)// 开启 activeRecord 模式
                        .setEnableCache(false)// XML 二级缓存
                        .setBaseResultMap(true)// XML ResultMap
                        .setBaseColumnList(true)// XML columList
                        .setOpen(false)//生成后打开文件夹
                        .setAuthor(authorName)
                        // 自定义文件命名，注意 %s 会自动填充表实体属性！
                        .setMapperName("%sMapper")
                        .setXmlName("%sMapper")
                        .setServiceName("%sService")
                        .setServiceImplName("%sServiceImpl")
                        .setControllerName("%sController")
        );
        mpg.setDataSource(
                // 数据源配置
                new DataSourceConfig()
                        .setDbType(DbType.MYSQL)// 数据库类型
                        .setTypeConvert(new MySqlTypeConvert() {
                            // 自定义数据库表字段类型转换【可选】
                            @Override
                            public PropertyInfo processTypeConvert(GlobalConfig globalConfig, String fieldType) {
                                System.out.println("转换类型：" + fieldType);
                                // if ( fieldType.toLowerCase().contains( "tinyint" ) ) {
                                //    return DbColumnType.BOOLEAN;
                                // }
                                return super.processTypeConvert(globalConfig,fieldType);
                            }
                        })
                        .setDriverName("com.mysql.jdbc.Driver")
                        .setUsername("funwl")
                        .setPassword("Abc$123456789")
                        .setUrl("jdbc:mysql://rm-bp10p278e99hn4o6oeo.mysql.rds.aliyuncs.com:3306/ed-shop?useUnicode=true&characterEncoding=utf8")
        );
        mpg.setStrategy(
                // 策略配置
                new StrategyConfig()
                        // .setCapitalMode(true)// 全局大写命名
                        //.setDbColumnUnderline(true)//全局下划线命名
                        .setTablePrefix(new String[]{prefix})// 此处可以修改为您的表前缀
                        .setNaming(NamingStrategy.underline_to_camel)// 表名生成策略
                        .setInclude(new String[]{table}) // 需要生成的表
                        .setRestControllerStyle(true)
        );
        mpg.setPackageInfo(
                // 包配置
                new PackageConfig()
                        //.setModuleName("User")
                        .setParent("com.mmall" + packageName)// 自定义包路径
//                        .setController("controller")// 这里是控制器包名，默认 web
                        .setController("controller.express")// --qty
                        .setEntity("model")
                        .setMapper("dao")
                        .setService("service")
                        .setServiceImpl("service.serviceImpl")
                //.setXml("mapper")
        );

        //FileOutConfig mapperConfig = getMapperConfig();
        FileOutConfig entityConfig = getEntityConfig();
        //FileOutConfig serviceConfig = getServiceConfig();
        //FileOutConfig serviceImplConfig = getServiceImplConfig();
        //FileOutConfig contolConfig = getContolConfig();
        FileOutConfig daoConfig = getDaoConfig();
        List<FileOutConfig> fileOutConfigs = new ArrayList<FileOutConfig>();
        //fileOutConfigs.add(mapperConfig);
        fileOutConfigs.add(entityConfig);
        //fileOutConfigs.add(serviceConfig);
        //fileOutConfigs.add(serviceImplConfig);
        //fileOutConfigs.add(contolConfig);
        fileOutConfigs.add(daoConfig);
        mpg.setCfg(
                // 注入自定义配置，可以在 VM 中使用 cfg.abc 设置的值
                new InjectionConfig() {
                    @Override
                    public void initMap() {
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("abc", this.getConfig().getGlobalConfig().getAuthor() );
                        this.setMap(map);
                    }
                }.setFileOutConfigList(fileOutConfigs)
        ); mpg.setTemplate(
                // 关闭默认 xml 生成，调整生成 至 根目录
                new TemplateConfig().setXml(null)
                        // 自定义模板配置，模板可以参考源码 /mybatis-plus/src/main/resources/template 使用 copy
                        // 至您项目 src/main/resources/template 目录下，模板名称也可自定义如下配置：
                        .setController(null)
                        .setEntity(null)
                        .setMapper(null)
                        .setService(null)
                        .setServiceImpl(null)
        );

        // 执行生成
        mpg.execute();

        // 打印注入设置，这里演示模板里面怎么获取注入内容【可无】
        System.err.println(mpg.getCfg().getMap().get("abc"));
    }

    public static FileOutConfig getMapperConfig() {
        FileOutConfig fileOutConfig = new FileOutConfig("/templates/mapper.xml.vm") {
            // 自定义输出文件目录
            @Override
            public String outputFile(TableInfo tableInfo) {
                return path + "\\src\\main\\resources\\mapper\\" + tableInfo.getEntityName() + "Mapper.xml";
            }

        };
        return fileOutConfig;
    }

    public static FileOutConfig getEntityConfig() {
        FileOutConfig fileOutConfig = new FileOutConfig("/templates/entity2.java.vm") {
            // 自定义输出文件目录
            @Override
            public String outputFile(TableInfo tableInfo) {
                return path +pathTwo+ "\\model\\" + tableInfo.getEntityName() +".java";
            }

        };
        return fileOutConfig;
    }

    public static FileOutConfig getServiceConfig() {
        FileOutConfig fileOutConfig = new FileOutConfig("/templates/service.java.vm") {
            // 自定义输出文件目录
            @Override
            public String outputFile(TableInfo tableInfo) {
                return path + pathTwo+"\\service\\" + tableInfo.getEntityName() + "Service.java";
            }

        };
        return fileOutConfig;
    }

    public static FileOutConfig getServiceImplConfig() {
        FileOutConfig fileOutConfig = new FileOutConfig("/templates/serviceImpl.java.vm") {
            // 自定义输出文件目录
            @Override
            public String outputFile(TableInfo tableInfo) {
                return path + pathTwo+"\\service\\serviceImpl\\" + tableInfo.getEntityName() + "ServiceImpl.java";
            }

        };
        return fileOutConfig;
    }

    public static FileOutConfig getContolConfig() {
//        FileOutConfig fileOutConfig = new FileOutConfig("/templates/controller.java.vm") {
        FileOutConfig fileOutConfig = new FileOutConfig("/templates/controller1.java.vm") {//--qty
            // 自定义输出文件目录
            @Override
            public String outputFile(TableInfo tableInfo) {
//                return path + pathTwo+"\\controller\\fn\\" + tableInfo.getEntityName() + "Controller.java";
                return path + pathTwo+"\\controller\\express\\" + tableInfo.getEntityName() + "Controller.java";//--qty
            }

        };
        return fileOutConfig;
    }

    public static FileOutConfig getDaoConfig() {
        FileOutConfig fileOutConfig = new FileOutConfig("/templates/mapper.java.vm") {
            // 自定义输出文件目录
            @Override
            public String outputFile(TableInfo tableInfo) {
                return path +pathTwo+ "\\dao\\" + tableInfo.getEntityName() + "Mapper.java";
            }

        };
        return fileOutConfig;
    }

}
