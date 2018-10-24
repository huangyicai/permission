package com.mmall.excel.thread;

import com.mmall.component.ApplicationContextHelper;
import com.mmall.dao.*;
import com.mmall.dto.ThreadDto;
import com.mmall.excel.Bill;
import com.mmall.excel.export.DataSheetExecute;
import com.mmall.excel.export.ExcelExportExecutor;
import com.mmall.model.*;
import com.mmall.util.RandomHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;


public class ThreadImport implements Callable<String> {

    private ThreadDto threadDto;

    public ThreadImport(ThreadDto threadDto) {
        this.threadDto = threadDto;
    }

    /**
     * 线程执行导出
     * @return
     * @throws Exception
     */
    public String call()  {

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
                String pubUrl = threadDto.getTime()+"/"
                        +threadDto.getCompanyName()+"/"
                        +threadDto.getName()+"/"
                        +threadDto.getKey()+"/"
                        +threadDto.getKey()+"-"+timeStr[0]+"年"+timeStr[1]+"月账单"+".xlsx";
                //生成创建路径
                String path=threadDto.getPathHead()+pubUrl;

                threadDto.setKey(threadDto.getKey()+"-"+timeStr[0]+"年"+timeStr[1]+"月");


                File file=new File(path);
                File fileParent = file.getParentFile();
                if (!fileParent.exists()) {
                    fileParent.mkdirs();
                }
                file.createNewFile();

                //生成下载路径
                String pathIpUrl=threadDto.getPath()+pubUrl;

                threadDto.setPath(pathIpUrl);
                threadDto.setPathHead(path);
                threadDto.setIdtime(keyId);
                outputStream = new FileOutputStream(path);
                workbook.write(outputStream);
                outputStream.close();
                workbook.close();
                record(threadDto);
            }

            public void listen(Row row, int rows) {
//                System.out.println("执行到了：<" + rows + "> 这一行");
            }
        };

        new ExcelExportExecutor<Bill>(strings, threadDto.getList(), dataSheetExecute, true).execute();

        return "ok";
    }

    /**
     * 向数据库记录数据
     * @param threadDto
     */
    public void record(ThreadDto threadDto){
        //获取相应的bean
        TotalMapper totalMapper = ApplicationContextHelper.getBeanClass(TotalMapper.class);
        WeightCalculateMapper weightCalculateMapper = ApplicationContextHelper.getBeanClass(WeightCalculateMapper.class);
        ProvinceCalculateMapper provinceCalculateMapper = ApplicationContextHelper.getBeanClass(ProvinceCalculateMapper.class);
        DailyTotalMapper dailyTotalMapper = ApplicationContextHelper.getBeanClass(DailyTotalMapper.class);

        //初始化数据
        Total total=new Total();

        WeightCalculate weightCalculate=new WeightCalculate();
        ProvinceCalculate provinceCalculate=new ProvinceCalculate();
        DailyTotal dailyTotal=new DailyTotal();


        //添加账单表数据
        total.setSumId(threadDto.getSumId());
        total.setName(threadDto.getKey());
        total.setUserId(threadDto.getId());
        total.setSendId(threadDto.getSendId());
        total.setTotalTime(threadDto.getTime());
        total.setTotalNumber(threadDto.getTotalNum());
        total.setTotalWeight(threadDto.getWeight());
        total.setOrderNo(threadDto.getIdtime());
        total.setTotalUrl(threadDto.getPath());
        total.setCdUrl(threadDto.getPathHead());
        total.setCreateTime(new Date());
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

        dailyTotal.setTotalId(total.getTotalId());
        dailyTotal.setDailyTime(threadDto.getDailyTime());
        dailyTotal.setDailyText(threadDto.getDaily());
        dailyTotalMapper.insert(dailyTotal);
    }
}
