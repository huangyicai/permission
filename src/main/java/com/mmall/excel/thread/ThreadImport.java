package com.mmall.excel.thread;

import com.mmall.component.ApplicationContextHelper;
import com.mmall.dao.DailyTotalMapper;
import com.mmall.dao.ProvincialMeterMapper;
import com.mmall.dao.TotalMapper;
import com.mmall.dao.WeightCalculateMapper;
import com.mmall.dto.ThreadDto;
import com.mmall.excel.Bill;
import com.mmall.excel.export.DataSheetExecute;
import com.mmall.excel.export.ExcelExportExecutor;
import com.mmall.model.DailyTotal;
import com.mmall.model.ProvincialMeter;
import com.mmall.model.Total;
import com.mmall.model.WeightCalculate;
import com.mmall.util.RandomHelper;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Date;


public class ThreadImport extends Thread {

    private ThreadDto threadDto;

    public ThreadImport(ThreadDto threadDto) {
        this.threadDto = threadDto;
    }

    /**
     * 线程执行导出
     * @return
     * @throws Exception
     */
    public void run()  {

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

                //生成随机码
                String time = new Date().getTime()+"";
                String keyId=time.substring(9,time.length())+ RandomHelper.getRandNum(3);

                //重名名账单
                String[] timeStr=threadDto.getTime().split("-");

                //生成账单中间路径
                String pubUrl = threadDto.getTime()+"/"
                        +threadDto.getCompanyName()+"/"
                        +threadDto.getName()+"/"
                        +threadDto.getKey()+"/"
                        +threadDto.getKey()+"-"+timeStr[0]+"年"+timeStr[1]+"月账单-"+keyId+".xlsx";

                //生成创建路径
                String path=threadDto.getPathHead()+pubUrl;

                threadDto.setKey(threadDto.getKey()+"-"+timeStr[0]+"年"+timeStr[1]+"月");

                File file=new File(path);

                //获取父目录
                File fileParent = file.getParentFile();

                //判断父目录是否存在，不存在则创建
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
        };

        new ExcelExportExecutor<Bill>(strings, threadDto.getList(), dataSheetExecute, true).execute();
    }

    /**
     * 向数据库记录数据
     * @param threadDto
     */
    private void record(ThreadDto threadDto){
        //获取相应的bean
        TotalMapper totalMapper = ApplicationContextHelper.getBeanClass(TotalMapper.class);
        WeightCalculateMapper weightCalculateMapper = ApplicationContextHelper.getBeanClass(WeightCalculateMapper.class);
        ProvincialMeterMapper provincialMeterMapper = ApplicationContextHelper.getBeanClass(ProvincialMeterMapper.class);
        DailyTotalMapper dailyTotalMapper = ApplicationContextHelper.getBeanClass(DailyTotalMapper.class);

        //初始化数据
        Total total=new Total();

        WeightCalculate weightCalculate=new WeightCalculate();
        ProvincialMeter provincialMeter=new ProvincialMeter();
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
        provincialMeter.setTotalId(total.getTotalId());
        provincialMeter.setMeterText(threadDto.getMd());
        provincialMeterMapper.insert(provincialMeter);

        dailyTotal.setTotalId(total.getTotalId());
        dailyTotal.setDailyTime(threadDto.getDailyTime());
        dailyTotal.setDailyText(threadDto.getDaily());
        dailyTotalMapper.insert(dailyTotal);
    }
}
