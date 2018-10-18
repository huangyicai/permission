package com.mmall.excel.imp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.google.common.collect.ArrayListMultimap;
import com.mmall.config.UserInfoConfig;
import com.mmall.constants.LevelConstants;
import com.mmall.dao.*;
import com.mmall.dto.ThreadDto;
import com.mmall.excel.Bill;
import com.mmall.excel.export.DataSheetExecute;
import com.mmall.excel.export.ExcelExportExecutor;
import com.mmall.excel.thread.ThreadImport;
import com.mmall.model.*;
import com.mmall.model.Response.InfoEnums;
import com.mmall.model.Response.Result;
import com.mmall.service.SysUserInfoService;
import com.mmall.util.LevelUtil;
import com.mmall.util.RandomHelper;
import org.apache.poi.ooxml.util.SAXHelper;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler.SheetContentsHandler;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFComment;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.math.BigDecimal;
import java.util.Date;
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

    @Autowired
    private BillKeywordMapper billKeywordMapper;

    @Autowired
    private TotalMapper totalMapper;

    @Autowired
    private SumTatalMapper sumTatalMapper;

    private final Logger logger = LoggerFactory.getLogger(XlsxProcessAbstract.class);

    //开始读取行数从第0行开始计算
    private final int minColumns = 0;

    //储存每一行数据
    private final StringBuffer rowStrs = new StringBuffer();

    //获取Excel每一行的执行类
    ProcessTransDetailDataDto processTransDetailData = new ProcessTransDetailDataDto();

    //根据重量分离
    public ArrayListMultimap<Integer, BigDecimal> weightMap = ArrayListMultimap.create();

    //根据省份分离数据
    public ArrayListMultimap<String, String> destination = ArrayListMultimap.create();

    /**
     * 根据路径读取数据
     * @param filename
     * @return
     * @throws Exception
     */
    public ArrayListMultimap<String, Bill>  processAllSheet(String filename) throws Exception {
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

        return processTransDetailData.map;
    }

    /**
     * 支持遍历同一个excle文件下多个sheet的解析
     * excel记录行操作方法，以行索引和行元素列表为参数，对一行元素进行操作，元素为String类型
     * @param xlsxFile
     * @return
     * @throws Exception
     */
    public void processAllSheet(MultipartFile xlsxFile,String time,Integer type,String sunTotalId) throws Exception {

        //替换
        if(type==2){

            //获取所替换的账单
            List<Total> totals = totalService.listTotal(time, sunTotalId,0);

            //删除总账单
            sumTatalMapper.deleteSumTotal(sunTotalId);

            if(totals!=null && totals.size()>0){
                //删除之前的账单
                totalService.delete(new QueryWrapper<Total>().eq("total_time",time));
                String idStr="";
                for(Total total:totals){
                    idStr+=total.getTotalId()+",";
                }
                idStr=idStr.substring(0,idStr.length()-1);
                weightCalculateMapper.deleteByTotalId(idStr);
                provinceCalculateMapper.deleteByTotalId(idStr);
            }
        }

        //判断文件夹是否存在，不存在则创建
        File file=new File(LevelConstants.REALPATH);
        if(!file .exists() && !file .isDirectory()){
            file.mkdir();
        }

        //获取用户信息
//        SysUserInfo user = (SysUserInfo) SecurityUtils.getSubject().getSession().getAttribute("user");
        SysUserInfo user = UserInfoConfig.getUserInfo();
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
        //獲取姓名集合
        List<BillKeyword> list = billKeywordMapper.getBillKeyword(nameStr);

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
        ExecutorService threadPool = Executors.newFixedThreadPool(4);

        //根据用户分表
        ArrayListMultimap<String, Bill> map = processTransDetailData.map;

        //获取上传的文件名字
        String fName = xlsxFile.getOriginalFilename();
        String[] str1= fName.split("\\.");

        //向总账单添加数据
        SumTatal sumTatal=new SumTatal();
        sumTatal.setSumName(str1[0]);
        sumTatal.setSumTime(time);
        sumTatal.setUserId(user.getId());
        sumTatalMapper.insert(sumTatal);

        for (String key:map.keySet()) {

            int id=-1;
            Integer total=0;//总单量
            BigDecimal weightOne=BigDecimal.ZERO;//总重

//            String ompPath=path+key+".xlsx";

            //判斷是否存在該用戶
            for(BillKeyword name:list){
                if(key.equals(name.getKeyword())){
                    id=name.getUserId();
                    break;
                }
            }

            //分离数据
            for (Bill bill:map.get(key)) {
                weightInterval(bill.getWeight());
                province(bill.getDestination());

                //计算每个月份的单量，总重量
                total+=1;
                weightOne=weightOne.add(bill.getWeight());
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

            ThreadDto threadDto=new ThreadDto();
            threadDto.setId(id);
            threadDto.setSendId(user.getId());
            threadDto.setKey(key);
            threadDto.setList(map.get(key));
            threadDto.setMd(md);
            threadDto.setMw(mw);
            threadDto.setPath(LevelConstants.OMPPATH);
            threadDto.setPathHead(LevelConstants.REALPATH);
            threadDto.setTime(time);
            threadDto.setTotalNum(total);
            threadDto.setWeight(weightOne);
            threadDto.setName(str1[0]);
            threadDto.setSumId(sumTatal.getSumId());
            threadDto.setCompanyName(user.getCompanyName());

            ThreadImport threadImport=new ThreadImport(threadDto);
            threadPool.submit(threadImport);

            destination.clear();
            weightMap.clear();
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
        map.clear();
    }

    /**
     * 重新上传
     * @param xlsxFile
     * @return
     * @throws Exception
     */
    public Result againSet(MultipartFile xlsxFile, Integer totalId) throws Exception {
        Map threadDto1 = getThreadDto(xlsxFile);
        ThreadDto threadDto = (ThreadDto) threadDto1.get("threadDto");
        ArrayListMultimap<String, Bill> map= (ArrayListMultimap<String, Bill>) threadDto1.get("map");
        Result result = updateTatal(threadDto, totalId);

        map.clear();
        destination.clear();
        weightMap.clear();
        return result;
    }

    /**
     * 客户追加上传
     * @param xlsxFile
     * @return
     * @throws Exception
     */
    public void additionalSet(MultipartFile xlsxFile,Integer userId,Integer type,String date) throws Exception {
        Map threadDto1 = getThreadDto(xlsxFile);
        ThreadDto threadDto = (ThreadDto) threadDto1.get("threadDto");
        ArrayListMultimap<String, Bill> map= (ArrayListMultimap<String, Bill>) threadDto1.get("map");

        threadDto.setTime(date);

        final Total total = new Total();

        String[] strings = {"商家名称", "扫描时间", "运单编号", "目的地", "快递重量"};

        DataSheetExecute<Bill> dataSheetExecute = new DataSheetExecute<Bill>() {

            public void execute(Row row, Bill personUser) {
                row.createCell(0).setCellValue(personUser.getBillName());
                row.createCell(1).setCellValue(personUser.getSweepTime());
                row.createCell(2).setCellValue(personUser.getSerialNumber());
                row.createCell(3).setCellValue(personUser.getDestination());
                row.createCell(4).setCellValue(personUser.getWeight().toString());
            }

            public void writeExcel(SXSSFWorkbook workbook, OutputStream outputStream) throws Exception {

                //生成随机码
                String time = new Date().getTime()+"";
                String keyId=time.substring(9,time.length())+ RandomHelper.getRandNum(3);

                //重名名账单
                String[] timeStr=threadDto.getTime().split("-");

                //生成创建路径
                String path=threadDto.getPathHead()+threadDto.getTime()+"/"+threadDto.getCompanyName()+"/"+threadDto.getName()+"/"+threadDto.getKey()+"/"+threadDto.getKey()+"-"+timeStr[0]+"年"+timeStr[1]+"月账单"+".xlsx";

                threadDto.setKey(threadDto.getKey()+"-"+timeStr[0]+"年"+timeStr[1]);


                File file=new File(path);
                File fileParent = file.getParentFile();
                if (!fileParent.exists()) {
                    fileParent.mkdirs();
                }
                file.createNewFile();

                //生成下载路径
                String pathIpUrl=threadDto.getPath()+threadDto.getKey()+"月账单"+".xlsx";

                threadDto.setPath(pathIpUrl);
                threadDto.setPathHead(path);
                threadDto.setIdtime(keyId);

                outputStream = new FileOutputStream(path);
                workbook.write(outputStream);
                outputStream.close();
                workbook.close();

                //初始化数据
                WeightCalculate weightCalculate=new WeightCalculate();
                ProvinceCalculate provinceCalculate=new ProvinceCalculate();

                //修改账单表数据
                total.setName(threadDto.getKey());
                total.setUserId(userId);
                total.setSendId(threadDto.getSendId());
                total.setTotalTime(threadDto.getTime());
                total.setTotalNumber(threadDto.getTotalNum());
                total.setTotalWeight(threadDto.getWeight());
                total.setOrderNo(threadDto.getIdtime());
                total.setTotalUrl(threadDto.getPath());
                total.setCdUrl(threadDto.getPathHead());
                total.setCreateTime(new Date());
                if(type==1){
                    total.setTotalState(1);
                }else{
                    total.setTotalState(-1);
                }
                totalMapper.insert(total);

                //修改重量区间数据
                weightCalculate.setTotalId(total.getTotalId());
                weightCalculate.setZero(threadDto.getMw().get(0));
                weightCalculate.setOne(threadDto.getMw().get(1));
                weightCalculate.setTwo(threadDto.getMw().get(2));
                weightCalculate.setThree(threadDto.getMw().get(3));
                weightCalculate.setFour(threadDto.getMw().get(4));
                weightCalculate.setFive(threadDto.getMw().get(5));
                weightCalculate.setSix(threadDto.getMw().get(6));
                weightCalculate.setSeven(threadDto.getMw().get(7));
                weightCalculate.setEight(threadDto.getMw().get(8));
                weightCalculate.setNine(threadDto.getMw().get(9));
                weightCalculate.setTen(threadDto.getMw().get(10));
                weightCalculate.setEleven(threadDto.getMw().get(11));
                weightCalculate.setTwelve(threadDto.getMw().get(12));
                weightCalculate.setThirteen(threadDto.getMw().get(13));
                weightCalculate.setFourteen(threadDto.getMw().get(14));
                weightCalculate.setFifteen(threadDto.getMw().get(15));
                weightCalculate.setSixteen(threadDto.getMw().get(16));
                weightCalculate.setSeventeen(threadDto.getMw().get(17));
                weightCalculate.setEighteen(threadDto.getMw().get(18));
                weightCalculate.setNineteen(threadDto.getMw().get(19));
                weightCalculate.setTwenty(threadDto.getMw().get(20));
                weightCalculate.setTwentyOne(threadDto.getMw().get(21));
                weightCalculateMapper.insert(weightCalculate);

                //修改省计表数据
                provinceCalculate.setTotalId(total.getTotalId());
                provinceCalculate.setBeijing(threadDto.getMd().get("北京"));
                provinceCalculate.setTianjing(threadDto.getMd().get("天津"));
                provinceCalculate.setHebei(threadDto.getMd().get("河北"));
                provinceCalculate.setShanxi(threadDto.getMd().get("山西"));
                provinceCalculate.setNeimenggu(threadDto.getMd().get("内蒙古"));
                provinceCalculate.setLiaoning(threadDto.getMd().get("辽宁"));
                provinceCalculate.setJiling(threadDto.getMd().get("吉林"));
                provinceCalculate.setHeilongjiang(threadDto.getMd().get("黑龙江"));
                provinceCalculate.setShanghai(threadDto.getMd().get("上海"));
                provinceCalculate.setJiangsu(threadDto.getMd().get("江苏"));
                provinceCalculate.setZhejaing(threadDto.getMd().get("浙江"));
                provinceCalculate.setAnhui(threadDto.getMd().get("安徽"));
                provinceCalculate.setFujian(threadDto.getMd().get("福建"));
                provinceCalculate.setJaingxi(threadDto.getMd().get("江西"));
                provinceCalculate.setShandong(threadDto.getMd().get("山东"));
                provinceCalculate.setHenan(threadDto.getMd().get("河南"));
                provinceCalculate.setHubei(threadDto.getMd().get("湖北"));
                provinceCalculate.setHunan(threadDto.getMd().get("湖南"));
                provinceCalculate.setGuangdong(threadDto.getMd().get("广东"));
                provinceCalculate.setGuangxi(threadDto.getMd().get("广西"));
                provinceCalculate.setHainan(threadDto.getMd().get("海南"));
                provinceCalculate.setChongqing(threadDto.getMd().get("重庆"));
                provinceCalculate.setSichuan(threadDto.getMd().get("四川"));
                provinceCalculate.setGuizhou(threadDto.getMd().get("贵州"));
                provinceCalculate.setYunnan(threadDto.getMd().get("云南"));
                provinceCalculate.setXizang(threadDto.getMd().get("西藏"));
                provinceCalculate.setShanxi(threadDto.getMd().get("陕西"));
                provinceCalculate.setGansu(threadDto.getMd().get("甘肃"));
                provinceCalculate.setQinghai(threadDto.getMd().get("青海"));
                provinceCalculate.setNingxia(threadDto.getMd().get("宁夏"));
                provinceCalculate.setXinjang(threadDto.getMd().get("新疆"));
                provinceCalculate.setTaiwan(threadDto.getMd().get("台湾"));
                provinceCalculate.setXianggang(threadDto.getMd().get("香港"));
                provinceCalculate.setAomen(threadDto.getMd().get("澳门"));
                provinceCalculateMapper.insert(provinceCalculate);
            }

            public void listen(Row row, int rows) {
//                System.out.println("执行到了：<" + rows + "> 这一行");
            }
        };

        new ExcelExportExecutor<Bill>(strings, threadDto.getList(), dataSheetExecute, true).execute();
        map.clear();
        destination.clear();
        weightMap.clear();
    }

    /**
     * 分离数据用户级别账单
     * @param xlsxFile
     * @return
     */
    public Map getThreadDto(MultipartFile xlsxFile) throws Exception {
        SysUserInfo userInfo = UserInfoConfig.getUserInfo();
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

        //获取上传的文件名字
        String fName = xlsxFile.getOriginalFilename();
        String[] str1= fName.split("\\.");

        //根据用户分表
        ArrayListMultimap<String, Bill> map = processTransDetailData.map;
        ThreadDto threadDto=new ThreadDto();
        for (String key:map.keySet()) {

            Integer total=0;//总单量
            BigDecimal weightOne=BigDecimal.ZERO;//总重

            //分离数据
            for (Bill bill:map.get(key)) {
                weightInterval(bill.getWeight());
                province(bill.getDestination());

                //计算每个月份的单量，总重量
                total+=1;
                weightOne=weightOne.add(bill.getWeight());
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

            threadDto.setSendId(userInfo.getId());
            threadDto.setKey(key);
            threadDto.setList(map.get(key));
            threadDto.setMd(md);
            threadDto.setMw(mw);
            threadDto.setPath(LevelConstants.OMPPATH);
            threadDto.setPathHead(LevelConstants.REALPATH);
            threadDto.setTotalNum(total);
            threadDto.setWeight(weightOne);
            threadDto.setName(str1[0]);
            threadDto.setCompanyName(userInfo.getCompanyName());
        }
        Map m=new HashMap<>();
        m.put("map",map);
        m.put("threadDto",threadDto);
        return m;
    }




    /**
     * 重新上传----写入新的Excel
     * @param threadDto
     */
    @Transactional
    public Result updateTatal(final ThreadDto threadDto,Integer totalId){

        final Total total = totalService.selectById(totalId);

        if(total!=null && total.getTotalState()>1){
            return Result.error(InfoEnums.NOT_UPDATE);
        }

        String[] strings = {"商家名称", "扫描时间", "运单编号", "目的地", "快递重量"};

        DataSheetExecute<Bill> dataSheetExecute = new DataSheetExecute<Bill>() {

            public void execute(Row row, Bill personUser) {
                row.createCell(0).setCellValue(personUser.getBillName());
                row.createCell(1).setCellValue(personUser.getSweepTime());
                row.createCell(2).setCellValue(personUser.getSerialNumber());
                row.createCell(3).setCellValue(personUser.getDestination());
                row.createCell(4).setCellValue(personUser.getWeight().toString());
            }

            public void writeExcel(SXSSFWorkbook workbook, OutputStream outputStream) throws Exception {
                outputStream = new FileOutputStream(total.getCdUrl());
                workbook.write(outputStream);
                outputStream.close();
                workbook.close();

                //初始化数据
                WeightCalculate weightCalculate=new WeightCalculate();
                ProvinceCalculate provinceCalculate=new ProvinceCalculate();

                //修改账单表数据
                total.setTotalId(totalId);
                total.setTotalNumber(threadDto.getTotalNum());
                total.setTotalWeight(threadDto.getWeight());
                total.setTotalOffer(BigDecimal.ZERO);
                total.setTotalCost(BigDecimal.ZERO);
                total.setTotalPaid(BigDecimal.ZERO);
                total.setTotalState(-1);
                totalMapper.updateById(total);

                //修改重量区间数据
                weightCalculate.setZero(threadDto.getMw().get(0));
                weightCalculate.setOne(threadDto.getMw().get(1));
                weightCalculate.setTwo(threadDto.getMw().get(2));
                weightCalculate.setThree(threadDto.getMw().get(3));
                weightCalculate.setFour(threadDto.getMw().get(4));
                weightCalculate.setFive(threadDto.getMw().get(5));
                weightCalculate.setSix(threadDto.getMw().get(6));
                weightCalculate.setSeven(threadDto.getMw().get(7));
                weightCalculate.setEight(threadDto.getMw().get(8));
                weightCalculate.setNine(threadDto.getMw().get(9));
                weightCalculate.setTen(threadDto.getMw().get(10));
                weightCalculate.setEleven(threadDto.getMw().get(11));
                weightCalculate.setTwelve(threadDto.getMw().get(12));
                weightCalculate.setThirteen(threadDto.getMw().get(13));
                weightCalculate.setFourteen(threadDto.getMw().get(14));
                weightCalculate.setFifteen(threadDto.getMw().get(15));
                weightCalculate.setSixteen(threadDto.getMw().get(16));
                weightCalculate.setSeventeen(threadDto.getMw().get(17));
                weightCalculate.setEighteen(threadDto.getMw().get(18));
                weightCalculate.setNineteen(threadDto.getMw().get(19));
                weightCalculate.setTwenty(threadDto.getMw().get(20));
                weightCalculate.setTwentyOne(threadDto.getMw().get(21));
                weightCalculateMapper.update(weightCalculate,new UpdateWrapper<WeightCalculate>().eq("total_id",total.getTotalId()));

                //修改省计表数据
                provinceCalculate.setBeijing(threadDto.getMd().get("北京"));
                provinceCalculate.setTianjing(threadDto.getMd().get("天津"));
                provinceCalculate.setHebei(threadDto.getMd().get("河北"));
                provinceCalculate.setShanxi(threadDto.getMd().get("山西"));
                provinceCalculate.setNeimenggu(threadDto.getMd().get("内蒙古"));
                provinceCalculate.setLiaoning(threadDto.getMd().get("辽宁"));
                provinceCalculate.setJiling(threadDto.getMd().get("吉林"));
                provinceCalculate.setHeilongjiang(threadDto.getMd().get("黑龙江"));
                provinceCalculate.setShanghai(threadDto.getMd().get("上海"));
                provinceCalculate.setJiangsu(threadDto.getMd().get("江苏"));
                provinceCalculate.setZhejaing(threadDto.getMd().get("浙江"));
                provinceCalculate.setAnhui(threadDto.getMd().get("安徽"));
                provinceCalculate.setFujian(threadDto.getMd().get("福建"));
                provinceCalculate.setJaingxi(threadDto.getMd().get("江西"));
                provinceCalculate.setShandong(threadDto.getMd().get("山东"));
                provinceCalculate.setHenan(threadDto.getMd().get("河南"));
                provinceCalculate.setHubei(threadDto.getMd().get("湖北"));
                provinceCalculate.setHunan(threadDto.getMd().get("湖南"));
                provinceCalculate.setGuangdong(threadDto.getMd().get("广东"));
                provinceCalculate.setGuangxi(threadDto.getMd().get("广西"));
                provinceCalculate.setHainan(threadDto.getMd().get("海南"));
                provinceCalculate.setChongqing(threadDto.getMd().get("重庆"));
                provinceCalculate.setSichuan(threadDto.getMd().get("四川"));
                provinceCalculate.setGuizhou(threadDto.getMd().get("贵州"));
                provinceCalculate.setYunnan(threadDto.getMd().get("云南"));
                provinceCalculate.setXizang(threadDto.getMd().get("西藏"));
                provinceCalculate.setShanxi(threadDto.getMd().get("陕西"));
                provinceCalculate.setGansu(threadDto.getMd().get("甘肃"));
                provinceCalculate.setQinghai(threadDto.getMd().get("青海"));
                provinceCalculate.setNingxia(threadDto.getMd().get("宁夏"));
                provinceCalculate.setXinjang(threadDto.getMd().get("新疆"));
                provinceCalculate.setTaiwan(threadDto.getMd().get("台湾"));
                provinceCalculate.setXianggang(threadDto.getMd().get("香港"));
                provinceCalculate.setAomen(threadDto.getMd().get("澳门"));
                provinceCalculateMapper.update(provinceCalculate,new UpdateWrapper<ProvinceCalculate>().eq("total_id",total.getTotalId()));
            }

            public void listen(Row row, int rows) {
//                System.out.println("执行到了：<" + rows + "> 这一行");
            }
        };

        new ExcelExportExecutor<Bill>(strings, threadDto.getList(), dataSheetExecute, true).execute();
        return Result.ok();
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
     * 读取excel行、列值（这里可以分成其他页面）
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
