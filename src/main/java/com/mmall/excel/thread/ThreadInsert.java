//package com.mmall.excel.thread;
//
//import com.mmall.component.ApplicationContextHelper;
//import com.mmall.dao.DailyTotalMapper;
//import com.mmall.dao.ProvinceCalculateMapper;
//import com.mmall.dao.TotalMapper;
//import com.mmall.dao.WeightCalculateMapper;
//import com.mmall.excel.imp.XlsxProcessAbstract;
//import com.mmall.model.ProvinceCalculate;
//import com.mmall.model.Total;
//import com.mmall.model.WeightCalculate;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.math.BigDecimal;
//import java.util.Date;
//import java.util.Map;
//import java.util.concurrent.Callable;
//
//public class ThreadInsert extends Thread{
//
//    private MultipartFile xlsxFile;
//    private String time;
//    private Integer type;
//    private String sunTotalId;
//
//    public ThreadInsert(MultipartFile xlsxFile, String time, Integer type, String sunTotalId) {
//        this.xlsxFile = xlsxFile;
//        this.time = time;
//        this.type = type;
//        this.sunTotalId = sunTotalId;
//    }
//
//    @Override
//    public void run() {
//        XlsxProcessAbstract xlsxProcessAbstract = ApplicationContextHelper.getBeanClass(XlsxProcessAbstract.class);
//        try {
//            xlsxProcessAbstract.processAllSheet(xlsxFile,time,1,null);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}
