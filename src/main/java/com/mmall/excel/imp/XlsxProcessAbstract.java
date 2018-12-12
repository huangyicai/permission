package com.mmall.excel.imp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.google.common.collect.ArrayListMultimap;
import com.mmall.component.ApplicationContextHelper;
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
import com.mmall.service.PrepaidService;
import com.mmall.service.SysUserInfoService;
import com.mmall.service.TotalService;
import com.mmall.util.DateUtils;
import com.mmall.util.LevelUtil;
import com.mmall.util.RandomHelper;
import com.mmall.util.StringToDateUtil;
import com.mmall.vo.PricingGroupVo;
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
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFComment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *  读取Excel
 * @author qty
 * @since 2018-09-19
 */
public class XlsxProcessAbstract {

    private SysUserInfoService sysUserInfoService;

    private TotalMapper totalService;

    private TotalService totalService1;

    private WeightCalculateMapper weightCalculateMapper;

    private ProvincialMeterMapper provincialMeterMapper;

    private BillKeywordMapper billKeywordMapper;

    private TotalMapper totalMapper;

    private SumTatalMapper sumTatalMapper;

    private DailyTotalMapper dailyTotalMapper;

    private PricingGroupMapper pricingGroupMapper;

    private SpecialPricingGroupMapper specialPricingGroupMapper;

    private PrepaidService prepaidService;

    private final Logger logger = LoggerFactory.getLogger(XlsxProcessAbstract.class);

    //开始读取行数从第0行开始计算
    private int minColumns = 0;

    //储存每一行数据
    private StringBuffer rowStrs = new StringBuffer();

    //根据店铺分离数据
    private ArrayListMultimap<String, Bill> map = ArrayListMultimap.create();

    //根据重量分离
    private ArrayListMultimap<Integer, BigDecimal> weightMap = ArrayListMultimap.create();

    //根据省份分离数据
    private ArrayListMultimap<String, String> destination = ArrayListMultimap.create();

    //根据省份分离数据
    private ArrayListMultimap<String, String> dailyMap = ArrayListMultimap.create();

    //获取异常信息
    private List<String> listError = new ArrayList<String>();

    //初始化用户状态
    private Integer pricing=-1;

    public XlsxProcessAbstract() {
        this.sysUserInfoService = ApplicationContextHelper.getBeanClass(SysUserInfoService.class);
        this.totalService = ApplicationContextHelper.getBeanClass(TotalMapper.class);
        this.weightCalculateMapper = ApplicationContextHelper.getBeanClass(WeightCalculateMapper.class);
        this.provincialMeterMapper = ApplicationContextHelper.getBeanClass(ProvincialMeterMapper.class);
        this.billKeywordMapper = ApplicationContextHelper.getBeanClass(BillKeywordMapper.class);
        this.totalMapper = ApplicationContextHelper.getBeanClass(TotalMapper.class);
        this.sumTatalMapper = ApplicationContextHelper.getBeanClass(SumTatalMapper.class);
        this.dailyTotalMapper = ApplicationContextHelper.getBeanClass(DailyTotalMapper.class);
        this.pricingGroupMapper = ApplicationContextHelper.getBeanClass(PricingGroupMapper.class);
        this.specialPricingGroupMapper = ApplicationContextHelper.getBeanClass(SpecialPricingGroupMapper.class);
        this.totalService1 = ApplicationContextHelper.getBeanClass(TotalService.class);
        this.prepaidService = ApplicationContextHelper.getBeanClass(PrepaidService.class);
    }


    /**
     * 根据路径读取数据
     * @param filename
     * @return
     * @throws Exception
     */
     public ArrayListMultimap<String, Bill> processAllSheet(String filename) throws Exception {
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
        return map;
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
            if(totals!=null && totals.size()>0){
                //删除总账单
                sumTatalMapper.deleteSumTotal(sunTotalId);
                //删除之前的账单
                totalService.deleteTotal(time,sunTotalId);
                String idStr="";
                for(Total total:totals){
                    idStr+=total.getTotalId()+",";
                }
                idStr=idStr.substring(0,idStr.length()-1);
                weightCalculateMapper.deleteByTotalId(idStr);
                provincialMeterMapper.deleteByTotalId(idStr);
                dailyTotalMapper.deleteByTotalId(idStr);
            }
        }

        //判断文件夹是否存在，不存在则创建
        File file=new File(LevelConstants.REALPATH);
        if(!file .exists() && !file .isDirectory()){
            file.mkdir();
        }

        //获取用户信息
        SysUserInfo user = UserInfoConfig.getUserInfo();
        String s = LevelUtil.calculateLevel(user.getLevel(), user.getId());
        List<SysUserInfo> list1 = sysUserInfoService.list(new QueryWrapper<SysUserInfo>()
                .like("level", s)
                .eq("platform_id", 3)
                .eq("status",1)
                .select("id"));

        String nameStr="";
        for(SysUserInfo sysUserInfo: list1){
            nameStr+=sysUserInfo.getId()+",";
        }

        //nameStr=nameStr.substring(0,nameStr.length()-1);

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
        ExecutorService threadPool = Executors.newFixedThreadPool(3);

        //根据用户分表
//        ArrayListMultimap<String, Bill> map = processTransDetailData.map;

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

            key=key.replace("\u00A0", "");

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
                daily(bill.getSweepTime(),bill.getSerialNumber());

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
            String[] proStr=LevelConstants.PROSTR;

            String mdStr="";
            for(String str:proStr){
                mdStr+=destination.get(str).size()+",";
            }
            //得到省计字符串
            mdStr=mdStr.substring(0,mdStr.length()-1);


            //根据时间分离数据
            String[] dailyOriginal=LevelConstants.DAILY_ORIGINAL;
            String dyStr="";
                Integer day=DateUtils.getDays(time);
            String[] arr=dailyOriginal;
            switch (day){
                case 30:arr=daily(dailyOriginal,1);
                    break;
                case 29:arr=daily(dailyOriginal,2);
                    break;
                case 28:arr=daily(dailyOriginal,3);
                    break;
            }
            for(String str:arr){
                String sss= time+"-"+str;
                dyStr+=dailyMap.get(sss).size()+",";
            }
            dyStr=dyStr.substring(0,dyStr.length()-1);

            ThreadDto threadDto=new ThreadDto();
            threadDto.setDaily(dyStr);
            threadDto.setDailyTime(time);
            threadDto.setId(id);
            threadDto.setSendId(user.getId());
            threadDto.setKey(key);
            threadDto.setList(map.get(key));
            threadDto.setMd(mdStr);
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
            dailyMap.clear();
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
    @Transactional
    public Result againSet(MultipartFile xlsxFile, Integer totalId) throws Exception {
        Total total = totalService.selectById(totalId);
        Map threadDto1 = getThreadDto(xlsxFile,total.getTotalTime());
        ThreadDto threadDto = (ThreadDto) threadDto1.get("threadDto");
        ArrayListMultimap<String, Bill> map= (ArrayListMultimap<String, Bill>) threadDto1.get("map");
        Result result = updateTatal(threadDto, total);

        map.clear();
        destination.clear();
        weightMap.clear();
        dailyMap.clear();
        pricing=-1;
        return result;
    }

    /**
     * 客户追加上传
     * @param xlsxFile
     * @return
     * @throws Exception
     */
    @Transactional
    public void additionalSet(MultipartFile xlsxFile,Integer userId,String date) throws Exception {
        SysUserInfo user = sysUserInfoService.getById( userId);
        Map threadDto1 = getThreadDto(xlsxFile,date);
        ThreadDto threadDto = (ThreadDto) threadDto1.get("threadDto");
        ArrayListMultimap<String, Bill> map= (ArrayListMultimap<String, Bill>) threadDto1.get("map");

        threadDto.setTime(date);

        final Total total = new Total();

        String[] strings = {"商家名称", "扫描时间", "运单编号", "目的地", "快递重量"};


        if(threadDto.getOff().compareTo(new BigDecimal("0"))==1){
            strings = new String[]{"商家名称", "扫描时间", "运单编号", "目的地", "快递重量", "报价"};
        }

        DataSheetExecute<Bill> dataSheetExecute = new DataSheetExecute<Bill>() {

//            public void execute(Row row, Bill personUser) {
//                row.createCell(0).setCellValue(personUser.getBillName());
//                row.createCell(1).setCellValue(personUser.getSweepTime());
//                row.createCell(2).setCellValue(personUser.getSerialNumber());
//                row.createCell(3).setCellValue(personUser.getDestination());
//                row.createCell(4).setCellValue(personUser.getWeight().toString());
//            }

            public void writeExcel(SXSSFWorkbook workbook, OutputStream outputStream) throws Exception {

                //生成随机码
                String time = new Date().getTime()+"";
                String keyId=time.substring(9,time.length())+ RandomHelper.getRandNum(3);

                //重名名账单
                String[] timeStr=threadDto.getTime().split("-");

                //生成创建路径
                String path=threadDto.getPathHead()+threadDto.getTime()+"/"+threadDto.getCompanyName()+"/"+user.getName()+"/"+"客户追加账单"+"/"+threadDto.getKey()+"-"+timeStr[0]+"年"+timeStr[1]+"月账单-"+keyId+".xlsx";

                threadDto.setKey(threadDto.getKey()+"-"+timeStr[0]+"年"+timeStr[1]+"月");

                File file=new File(path);
                File fileParent = file.getParentFile();
                if (!fileParent.exists()) {
                    fileParent.mkdirs();
                }
                file.createNewFile();

                //生成下载路径
                String pathIpUrl=threadDto.getPath()+threadDto.getTime()+"/"+threadDto.getCompanyName()+"/"+user.getName()+"/"+"客户追加账单"+"/"+threadDto.getKey()+"账单-"+keyId+".xlsx";
                threadDto.setPath(pathIpUrl);
                threadDto.setPathHead(path);

                threadDto.setIdtime(keyId);

                outputStream = new FileOutputStream(path);
                workbook.write(outputStream);
                outputStream.close();
                workbook.close();

                //初始化数据
                WeightCalculate weightCalculate=new WeightCalculate();
                ProvincialMeter provincialMeter=new ProvincialMeter();
                DailyTotal dailyTotal=new DailyTotal();

                //计算预付
                Prepaid byId = prepaidService.getOne(new QueryWrapper<Prepaid>().eq("user_id",userId));
                BigDecimal money=BigDecimal.ZERO;
                if(byId!=null && byId.getMoney().compareTo(new BigDecimal("0"))>0){
                    money=byId.getMoney().multiply(new BigDecimal(threadDto.getTotalNum().toString()));
                }

                //添加账单表数据
                total.setName(threadDto.getKey());
                total.setUserId(userId);
                total.setSendId(threadDto.getSendId());
                total.setTotalTime(threadDto.getTime());
                total.setTotalNumber(threadDto.getTotalNum());
                total.setTotalWeight(threadDto.getWeight());
                total.setOrderNo(threadDto.getIdtime());
                total.setTotalUrl(pathIpUrl);
                total.setCdUrl(path);
                total.setCreateTime(new Date());
                total.setTotalState(pricing);
                total.setTotalOffer(threadDto.getOff());
                total.setTotalCost(threadDto.getCost());
                total.setTotalPaid(money);
                totalMapper.insert(total);

                //添加重量区间数据
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
                weightCalculateMapper.insert(weightCalculate);

                //添加省计表数据
                provincialMeter.setTotalId(total.getTotalId());
                provincialMeter.setMeterText(threadDto.getMd());
                provincialMeterMapper.insert(provincialMeter);

                //添加每日单量数据
                dailyTotal.setTotalId(total.getTotalId());
                dailyTotal.setDailyTime(threadDto.getDailyTime());
                dailyTotal.setDailyText(threadDto.getDaily());
                dailyTotalMapper.insert(dailyTotal);
            }

        };

        new ExcelExportExecutor<Bill>(strings, threadDto.getList(), dataSheetExecute, true).execute();
        map.clear();
        destination.clear();
        weightMap.clear();
        dailyMap.clear();
        pricing=-1;
    }

    /**
     * 分离数据用户级别账单
     * @param xlsxFile
     * @return
     */
    @Transactional
    public Map getThreadDto(MultipartFile xlsxFile,String time) throws Exception {
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

        //创建数据集合
        ThreadDto threadDto=new ThreadDto();

        Integer total=0;//总单量
        BigDecimal weightOne=BigDecimal.ZERO;//总重
        BigDecimal pri=BigDecimal.ZERO;
        BigDecimal cos=BigDecimal.ZERO;

        List<Bill> list=new ArrayList<>();

        for (String key:map.keySet()) {
            list.addAll(map.get(key));
        }


        //判断并计算成本
        SysUserInfo byId = sysUserInfoService.getById(userInfo.getId());
        if(byId.getPricingStatus()==1){

            //获取成本表
            List<PricingGroupVo> pricingOffer = pricingGroupMapper.ListPricingGroup(userInfo.getId());

            //获取特殊成本表
            List<PricingGroupVo> special1 = specialPricingGroupMapper.getPricingGroupVo(userInfo.getId());

            //计算成本
            list= totalService1.getCalculate(pricingOffer,1,list,special1);

        }

        //分离数据并计算成本
        for (Bill bill:list) {
            weightInterval(bill.getWeight());
            province(bill.getDestination());
            daily(bill.getSweepTime(),bill.getSerialNumber());
            //计算每个月份的单量，总重量
            total+=1;
            weightOne=weightOne.add(bill.getWeight());

            //读取报价
            if(pricing==1){
                pri=pri.add(bill.getOffer());
            }

            //计算成本
            if(byId.getPricingStatus()==1){
                cos=cos.add(bill.getCost());
            }
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
        String[] proStr=LevelConstants.PROSTR;

        String mdStr="";
        for(String str:proStr){
            mdStr+=destination.get(str).size()+",";
        }
        //得到省计字符串
        mdStr=mdStr.substring(0,mdStr.length()-1);

        //根据时间分离数据
        String[] dailyOriginal=LevelConstants.DAILY_ORIGINAL;
        String dyStr="";
        Integer day=DateUtils.getDays(time);
        String[] arr=dailyOriginal;
        switch (day){
            case 30:arr=daily(dailyOriginal,1);
                break;
            case 29:arr=daily(dailyOriginal,2);
                break;
            case 28:arr=daily(dailyOriginal,3);
                break;
        }

        for(String str:arr){
            String sss= time+"-"+str;
            dyStr+=dailyMap.get(sss).size()+",";
        }

        dyStr=dyStr.substring(0,dyStr.length()-1);
        threadDto.setCost(cos);
        threadDto.setOff(pri);
        threadDto.setDaily(dyStr);
        threadDto.setDailyTime(time);
        threadDto.setSendId(userInfo.getId());
        threadDto.setKey(str1[0]);
        threadDto.setList(list);
        threadDto.setMd(mdStr);
        threadDto.setMw(mw);
        threadDto.setPath(LevelConstants.OMPPATH);
        threadDto.setPathHead(LevelConstants.REALPATH);
        threadDto.setTime(time);
        threadDto.setTotalNum(total);
        threadDto.setWeight(weightOne);
        threadDto.setName(userInfo.getName());
        threadDto.setCompanyName(userInfo.getCompanyName());

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
    public Result updateTatal(final ThreadDto threadDto,Total total){

        if(total!=null && total.getTotalState()>1){
            return Result.error(InfoEnums.NOT_UPDATE);
        }

//        //重名名账单
//        String[] timeStr=threadDto.getTime().split("-");

        //生成下载路径
        String path=total.getCdUrl();
        String replace = path.replace(LevelConstants.REALPATH, LevelConstants.OMPPATH);
        String[] strings = {"商家名称", "扫描时间", "运单编号", "目的地", "快递重量"};

        DataSheetExecute<Bill> dataSheetExecute = new DataSheetExecute<Bill>() {
//            public void execute(Row row, Bill personUser) {
//                row.createCell(0).setCellValue(personUser.getBillName());
//                row.createCell(1).setCellValue(personUser.getSweepTime());
//                row.createCell(2).setCellValue(personUser.getSerialNumber());
//                row.createCell(3).setCellValue(personUser.getDestination());
//                row.createCell(4).setCellValue(personUser.getWeight().toString());
//            }

            public void writeExcel(SXSSFWorkbook workbook, OutputStream outputStream) throws Exception {

                File file=new File(total.getCdUrl());

                //获取父目录
                File fileParent = file.getParentFile();

                //判断父目录是否存在，不存在则创建
                if (!fileParent.exists()) {
                    fileParent.mkdirs();
                }

                file.createNewFile();

                outputStream = new FileOutputStream(total.getCdUrl());
                workbook.write(outputStream);
                outputStream.close();
                workbook.close();

                //初始化数据
                WeightCalculate weightCalculate=new WeightCalculate();
                ProvincialMeter provincialMeter=new ProvincialMeter();
                DailyTotal dailyTotal=new DailyTotal();

                //计算预付
                Prepaid byId = prepaidService.getOne(new QueryWrapper<Prepaid>().eq("user_id",(total.getUserId())));
                BigDecimal money=BigDecimal.ZERO;
                if(byId!=null && byId.getMoney().compareTo(new BigDecimal("0"))>0){
                    money=byId.getMoney().multiply(new BigDecimal(threadDto.getTotalNum().toString()));
                }

                //修改账单表数据
                total.setTotalId(total.getTotalId());
//                total.setName(threadDto.getKey());
                total.setTotalNumber(threadDto.getTotalNum());
                total.setTotalWeight(threadDto.getWeight());
                total.setTotalOffer(threadDto.getOff());
                total.setTotalCost(threadDto.getCost());
                total.setTotalPaid(money);
                total.setTotalUrl(replace);
                total.setTotalState(pricing);
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
                weightCalculateMapper.update(weightCalculate,new UpdateWrapper<WeightCalculate>().eq("total_id",total.getTotalId()));

                //修改省计表数据
                provincialMeter.setTotalId(total.getTotalId());
                provincialMeter.setMeterText(threadDto.getMd());
                provincialMeterMapper.update(provincialMeter,new UpdateWrapper<ProvincialMeter>().eq("total_id",total.getTotalId()));

                //修改每日单量数据
                dailyTotal.setDailyText(threadDto.getDaily());
                dailyTotal.setDailyTime(threadDto.getDailyTime());
                dailyTotalMapper.update(dailyTotal,new UpdateWrapper<DailyTotal>().eq("total_id",total.getTotalId()));
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
    private void parserSheetXml(StylesTable styles, ReadOnlySharedStringsTable strings, SheetContentsHandler sheetHandler, InputStream sheetInputStream) throws IOException, SAXException {
        DataFormatter formatter = new DataFormatter();
        InputSource sheetSource = new InputSource(sheetInputStream);
        try {
            XMLReader sheetParser = SAXHelper.newXMLReader();
            ContentHandler handler = new XSSFSheetXMLHandler(styles, null, strings, sheetHandler, formatter, false);
            sheetParser.setContentHandler(handler);
            sheetParser.parse(sheetSource);
            int i = strings.getCount();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException("SAX parser appears to be broken - " + e);
        }
    }

    /**
     * 读取excel行、列值（这里可以分成其他页面）
     */
     public class SheetToCSV implements SheetContentsHandler {
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
                String[] cellStrs = endRowStrs.split("\\|@\\|");
                if(currentRowNumber==0 ){

                    if(cellStrs.length==6){
                        pricing=1;
                        String str="";
//                        if(){
//
//                        }
                    }
                }else{
                    String nameStr=cellStrs[0];

                    //分表--为相应表格添加数据
                    Bill bill=new Bill();
                    bill.setBillName(cellStrs[0].replaceAll("\\s{2,}", ""));
                    bill.setSweepTime(cellStrs[1]);
                    bill.setSerialNumber(cellStrs[2]);
                    bill.setDestination(cellStrs[3]);
                    bill.setWeight(new BigDecimal(cellStrs[4]));

                    if(pricing==1){
                        bill.setOffer(new BigDecimal(cellStrs[5]));
                    }

                    //todo 后续分表按照名字+时间，可实现按用户和时间的分表
                    map.put(nameStr,bill);
                }
            }
            rowStrs.delete(0, rowStrs.length());// 清空buffer
        }

        public void cell(String cellReference, String cellValue, XSSFComment comment) {
            if("".equals(cellValue) || cellValue==null){
                listError.add(minColumns+"行：有数据为空");
            }
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
    private void weightInterval(BigDecimal weight){
        Integer intervalNum=0;
        Double[] interval=LevelConstants.INTERVAL;

        //判断重量所在区间
        for (int i = 0; i < interval.length; i++) {
            int Less= 0;
            int greater = weight.compareTo(new BigDecimal(interval[i].toString()));

            if(i < interval.length-1){
                Less=weight.compareTo(new BigDecimal(interval[i+1].toString()));
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
    private boolean province(String province){

        String[] proStr=LevelConstants.PROSTR;

        for (String str:proStr){
            if(province.startsWith(str)){
                destination.put(str,province);
                return true;
            }
        }

        return false;
    }

    /**
     * 根据每日数据进行分离
     */
    private void daily(String sweepTime,String num){

        if (sweepTime!=null) {
            Pattern p = Pattern.compile("\t|\r|\n");
            Matcher m = p.matcher(sweepTime);
            sweepTime = m.replaceAll("");
        }
        Date date=null;
        String format="";
        String days="";
        try{
            date = StringToDateUtil.stringToDate(sweepTime);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            format = sdf.format(date);
            days = DateUtils.getDays(format)+"";
        }catch (Exception e){
            format="未识别时间单号";
            days=num;
        }
        dailyMap.put(format,days);
    }

    /**
     * 截取时间数组
     * @param dailyOriginal
     * @param length
     * @return
     */
    private String[] daily(String[] dailyOriginal,Integer length){
        //新建数组
        String[] arr = new String[dailyOriginal.length-length];
        //将原数组数据赋值给新数组
        for(int j = 0;j<dailyOriginal.length-length;j++){
            arr[j] = dailyOriginal[j];
        }
        return arr;
    }

    public static void main(String[] args) {
        try {
            String pathIpUrl = new String("哈哈哈".getBytes("gbk"));
            System.out.println("哈哈哈");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}

