package com.mmall.excel.imp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.ArrayListMultimap;
import com.mmall.dao.ProvinceCalculateMapper;
import com.mmall.dao.TotalMapper;
import com.mmall.dao.WeightCalculateMapper;
import com.mmall.excel.Bill;
import com.mmall.excel.thread.ThreadImport;
import com.mmall.excel.thread.ThreadInsert;
import com.mmall.model.SysUserInfo;
import com.mmall.service.SysUserInfoService;
import org.apache.poi.ooxml.util.SAXHelper;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler.SheetContentsHandler;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFComment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *  读取Excel
 * @author qty
 * @since 2018-09-19
 */
@Component
public class XlsxProcessAbstract {

    @Autowired
    private SysUserInfoService sysUserInfoService;

    @Autowired
    private TotalMapper totalService;

    @Autowired
    private WeightCalculateMapper weightCalculateMapper;

    @Autowired
    private ProvinceCalculateMapper provinceCalculateMapper;

    private final Logger logger = LoggerFactory.getLogger(XlsxProcessAbstract.class);

    private final int minColumns = 0; //开始读取行数从第0行开始计算
    private final StringBuffer rowStrs = new StringBuffer();

    ProcessTransDetailDataDto processTransDetailData = new ProcessTransDetailDataDto();

    //根据重量分离
    public ArrayListMultimap<Integer, BigDecimal> weightMap = ArrayListMultimap.create();

    //根据省份分离数据
    public ArrayListMultimap<String, String> destination = ArrayListMultimap.create();



    /**
     * 支持遍历同一个excle文件下多个sheet的解析
     * excel记录行操作方法，以行索引和行元素列表为参数，对一行元素进行操作，元素为String类型
     * @param filename
     * @return
     * @throws Exception
     */
    public Map<String,String> processAllSheet(String filename,String time) throws Exception {

        //獲取姓名集合
        List<SysUserInfo> list = sysUserInfoService.list(new QueryWrapper<SysUserInfo>().select("name"));

        //判斷用戶是否存在的字段
        Integer type=1;

        OPCPackage pkg = OPCPackage.open(filename, PackageAccess.READ);
        ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(pkg);
        XSSFReader xssfReader = new XSSFReader(pkg);
        StylesTable styles = xssfReader.getStylesTable();
        XSSFReader.SheetIterator iter = (XSSFReader.SheetIterator) xssfReader.getSheetsData();
        InputStream stream = null;
        while (iter.hasNext()) {
            try {
                stream = iter.next();
                parserSheetXml(styles, strings, new SheetToCSV(), stream);
            } catch (Exception e) {
            } finally {
                stream.close();
            }
        }

        //创建线程池
        ExecutorService threadPool = Executors.newFixedThreadPool(10);

        //根据用户分表
        ArrayListMultimap<String, Bill> map = processTransDetailData.map;
        Map<String,String> urlMap=new HashMap<String,String>();
        for (String key:map.keySet()) {
            String path="E:/EDW/"+key+".xlsx";

            //判斷是否存在該用戶
            for(SysUserInfo name:list){
                if(key.equals(name.getName())){
                    path="E:/GDW/"+key+".xlsx";
                    type=2;
                    break;
                }
            }

            ThreadImport threadImport=new ThreadImport(path,map.get(key),key);
            threadPool.submit(threadImport);

            //分离数据
            for (Bill bill:map.get(key)) {
                weightInterval(bill.getWeight());
                province(bill.getDestination());
            }

            //根据重量分离数据
            Map<Integer,BigDecimal> mw=new HashMap<Integer, BigDecimal>();
            for (Integer integer:weightMap.keySet()) {
                BigDecimal weight=BigDecimal.ZERO;
                for (BigDecimal bigDecimal:weightMap.get(integer)){
                    weight=weight.add(bigDecimal);
                }
                mw.put(integer,weight);
            }

            //根据省份分离数据
            Map<String,Integer> md=new HashMap<String,Integer>();
            for(String str:destination.keySet()){
                md.put(str,destination.get(str).size());
            }

            //启动线程，向数据库插入数据
            ThreadInsert threadInsert=new ThreadInsert(key,mw,md,time,processTransDetailData.total,processTransDetailData.weight,type);
            threadInsert.setTotalService(totalService);
            threadInsert.setProvinceCalculateMapper(provinceCalculateMapper);
            threadInsert.setWeightCalculateMapper(weightCalculateMapper);
            threadPool.submit(threadInsert);

            destination.clear();
            weightMap.clear();
            urlMap.put(key,path);
        }

        //判断线程是否执行完毕
        threadPool.shutdown();
        while (true){
            if (threadPool.isTerminated()) {
                logger.info("线程已执行完毕");
                break;
            }
            Thread.sleep(200);
        }

        urlMap.put("total",processTransDetailData.total.toString());
        urlMap.put("weight",processTransDetailData.weight.toString());
        return urlMap;
    }

    /**
     * 支持遍历同一个excle文件下多个sheet的解析
     * excel记录行操作方法，以行索引和行元素列表为参数，对一行元素进行操作，元素为String类型
     * @param xlsxFile
     * @return
     * @throws Exception
     */
    public Map<String,String> processAllSheet(MultipartFile xlsxFile,String time) throws Exception {

        //獲取姓名集合
        List<SysUserInfo> list = sysUserInfoService.list(new QueryWrapper<SysUserInfo>().select("name"));

        Integer id=0;

        OPCPackage pkg = OPCPackage.open(xlsxFile.getInputStream());
        ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(pkg);
        XSSFReader xssfReader = new XSSFReader(pkg);
        StylesTable styles = xssfReader.getStylesTable();
        XSSFReader.SheetIterator iter = (XSSFReader.SheetIterator) xssfReader.getSheetsData();
        InputStream stream = null;
        while (iter.hasNext()) {
            try {
                stream = iter.next();
                parserSheetXml(styles, strings, new SheetToCSV(), stream);
            } catch (Exception e) {
                logger.error("parserSheetXml error: ",e);
            } finally {
                stream.close();
            }
        }

        //创建线程池
        ExecutorService threadPool = Executors.newFixedThreadPool(10);

        //根据用户分表
        ArrayListMultimap<String, Bill> map = processTransDetailData.map;
        Map<String,String> urlMap=new HashMap<String,String>();
        for (String key:map.keySet()) {
            String path="E:/GDW/"+key+".xlsx";

            //判斷是否存在該用戶
            for(SysUserInfo name:list){
                if(key.equals(name.getName())){
                    id=2;
                    break;
                }
            }

            ThreadImport threadImport=new ThreadImport(path,map.get(key),key);
            threadPool.submit(threadImport);

            //分离数据
            for (Bill bill:map.get(key)) {
                weightInterval(bill.getWeight());
                province(bill.getDestination());
            }

            //根据重量分离数据
            Map<Integer,BigDecimal> mw=new HashMap<Integer, BigDecimal>();
            for (Integer integer:weightMap.keySet()) {
                BigDecimal weight=BigDecimal.ZERO;
                for (BigDecimal bigDecimal:weightMap.get(integer)){
                    weight=weight.add(bigDecimal);
                }
                mw.put(integer,weight);
            }

            //根据省份分离数据
            Map<String,Integer> md=new HashMap<String,Integer>();
            for(String str:destination.keySet()){
                md.put(str,destination.get(str).size());
            }

            //启动线程，向数据库插入数据
            ThreadInsert threadInsert=new ThreadInsert(key,mw,md,time,processTransDetailData.total,processTransDetailData.weight,id);
            threadInsert.setTotalService(totalService);
            threadInsert.setProvinceCalculateMapper(provinceCalculateMapper);
            threadInsert.setWeightCalculateMapper(weightCalculateMapper);
            threadPool.submit(threadInsert);

            destination.clear();
            weightMap.clear();
            urlMap.put(key,path);
        }

        //判断线程是否执行完毕
        threadPool.shutdown();
        while (true){
            if (threadPool.isTerminated()) {
                logger.info("线程已执行完毕");
                break;
            }
            Thread.sleep(200);
        }

        urlMap.put("total",processTransDetailData.total.toString());
        urlMap.put("weight",processTransDetailData.weight.toString());
        return urlMap;
    }

    /**
     * 解析excel 转换成xml
     *
     * @param styles
     * @param strings
     * @param sheetHandler
     * @param sheetInputStream
     * @throws IOException
     * @throws SAXException
     */
    public void parserSheetXml(StylesTable styles, ReadOnlySharedStringsTable strings, SheetContentsHandler sheetHandler, InputStream sheetInputStream) throws IOException, SAXException {
        DataFormatter formatter = new DataFormatter();
        InputSource sheetSource = new InputSource(sheetInputStream);
        try {
            XMLReader sheetParser = SAXHelper.newXMLReader();
            ContentHandler handler = new XSSFSheetXMLHandler(styles, null, strings, sheetHandler, formatter, false);
            sheetParser.setContentHandler(handler);
            sheetParser.parse(sheetSource);
        } catch (ParserConfigurationException e) {
            throw new RuntimeException("SAX parser appears to be broken - " + e);
        }
    }

    /**
     * 读取excel行、列值
     */
    private class SheetToCSV implements SheetContentsHandler {
        private boolean firstCellOfRow = false;
        private int currentRowNumber = -1;
        private int currentColNumber = -1;

        /**
         * 处理cell中为空值的情况
         * @param number
         */
        private void processCellBlankCells(int number) {
            for (int i = 0; i < number; i++) {
                for (int j = 0; j < minColumns; j++) {
                    rowStrs.append("|@|");
                }
                rowStrs.append('\n');
            }
        }

        public void startRow(int rowNum) {
            processCellBlankCells(rowNum - currentRowNumber - 1);
            firstCellOfRow = true;
            currentRowNumber = rowNum;
            currentColNumber = -1;
        }

        public void endRow(int rowNum) {
            for (int i = currentColNumber; i < minColumns; i++) {
                rowStrs.append("|@|");
            }
            // 从设置的rowIndex的行数开始加入到list，前三行为标题，多个sheet都从第三行开始读取的数据加入到list
            String endRowStrs=rowStrs.toString();

            if(!rowStrs.toString().equals("|@|")) {
                processTransDetailData.processTransTotalData(endRowStrs, currentRowNumber);
            }
            rowStrs.delete(0, rowStrs.length());// 清空buffer
        }

        public void cell(String cellReference, String cellValue, XSSFComment comment) {
            if (firstCellOfRow) {
                firstCellOfRow = false;
            } else {
                rowStrs.append("|@|");
            }
            if (cellReference == null) {
                cellReference = new CellAddress(currentRowNumber, currentColNumber).formatAsString();
            }
            int thisCol = (new CellReference(cellReference)).getCol();
            int missedCols = thisCol - currentColNumber - 1;
            for (int i = 0; i < missedCols; i++) {
                // excel中为空的值设置为“|@|”
                rowStrs.append("|@|");
            }
            currentColNumber = thisCol;
            rowStrs.append(cellValue);
        }

        public void headerFooter(String text, boolean isHeader, String tagName) {
        }

    }

    /**
     * 获取重量区间
     * @param weight
     */
    public void weightInterval(BigDecimal weight){

        Integer intervalNum=0;
        Double[] interval={0.01,0.5,1.0,2.0,3.0,4.0,5.0,6.0,7.0,8.0,9.0,10.0,11.0,12.0,13.0,14.0,15.0,16.0,17.0,18.0,19.0,20.0};

        //判断重量所在区间
        for (int i = 0; i < interval.length; i++) {
            int Less= 0;
            int greater = weight.compareTo(new BigDecimal(interval[i]));

            if(i < interval.length-1){
                Less=weight.compareTo(new BigDecimal(interval[i+1]));
            }

            if(greater>=0 && Less<=0){
                intervalNum=i+1;
                break;
            }
        }

        weightMap.put(intervalNum,weight);
    }

    /**
     * 获取城市单件：http://www.tcmap.com.cn/list/jiancheng_list.html
     * @param province
     */
    public boolean province(String province){

        String[] proStr={"北京","天津","河北","山西","内蒙古","辽宁","吉林","黑龙江","上海","江苏","浙江","安徽","福建",
                "江西","山东","河南","湖北","湖南","广东","广西","海南","重庆","四川","贵州","云南","西藏","陕西","甘肃",
                "青海","宁夏","新疆","台湾","香港","澳门"};

        for (String str:proStr){
            if(province.startsWith(str)){
                destination.put(str,province);
                return true;
            }
        }

        return false;
    }
}
