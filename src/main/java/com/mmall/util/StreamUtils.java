package com.mmall.util;

import com.mmall.excel.Bill;
import com.mmall.excel.export.DataSheetExecute;
import com.mmall.excel.export.ExcelExportExecutor;
import net.sf.json.JSONArray;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.*;
import java.io.*;
import java.net.*;


@RestController
@RequestMapping(value = "/excel")
public class StreamUtils{


    /**
     * 百万级数据导出
     * @param num
     */
    @RequestMapping(value = "/getExcel")
    public void get(Integer num){

        //制造数据
        List<Bill> data = new ArrayList<Bill>();
        for (int i = 0; i < num; i++) {
            data.add(new Bill("金XX111"
                    , "2018-9-20" + i
                    , "男:" + i
                    , "137****2152:" + i
                    ,  new BigDecimal(i)));
        }

        //todo 直接导出
        final String path="E:/GDW"+ File.separator + "百万级数据导出.xlsx";
        String[] strings = {"商家名称", "扫描时间", "运单编号", "目的地", "快递重量"};
        DataSheetExecute<Bill> dataSheetExecute = new DataSheetExecute<Bill>() {

            public void execute(Row row, Bill personUser) {
                row.createCell(0).setCellValue(personUser.getBillName());
                row.createCell(1).setCellValue(personUser.getSweepTime());
                row.createCell(2).setCellValue(personUser.getSerialNumber());
                row.createCell(3).setCellValue(personUser.getDestination());
                row.createCell(4).setCellValue(personUser.getWeight().toString());
//                row.createCell(5).setCellValue(personUser.getWeight().toString());
            }

            public void writeExcel(SXSSFWorkbook workbook, OutputStream outputStream) throws Exception {
                outputStream = new FileOutputStream(path);
                workbook.write(outputStream);
            }

            public void listen(Row row, int rows) {
//                System.out.println("执行到了：<" + rows + "> 这一行");
            }
        };


        new ExcelExportExecutor<Bill>(strings, data, dataSheetExecute, true).execute();

    }

    @PostMapping(value = "/get1")
    public void get1(Integer num) throws Exception {
        //制造数据
        List<Bill> packagesArray = new ArrayList<Bill>();
        for (int i = 0; i < num; i++) {
            packagesArray.add(new Bill("金XX111"
                    , "2018-9-20" + i
                    , "男:" + i
                    , "137****2152:" + i
                    ,  new BigDecimal(i)));
        }

        String[] strings = new String[packagesArray.size()];

        packagesArray.toArray(strings);

        JSONArray.fromObject(packagesArray);

        // 将一个对象序列化为一个字符串
        String str = SerializeObjectToString(packagesArray);

        // 保存到文件 saveToFile(str);

        // 从文件读取字符串 str = readFromFile()

        // 将一个字符串反序化为一个对象
        packagesArray = (List<Bill>) UnserializeStringToObject(str);

//        for (Bill bill:packagesArray){
//            System.out.println(bill.getBillName()+"---"+bill.getDestination()+"---"+bill.getSerialNumber());
//        }
    }

    public static String SerializeObjectToString(Object object) throws Exception{

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(object);
        String serStr = byteArrayOutputStream.toString("ISO-8859-1");//必须是ISO-8859-1
        serStr = URLEncoder.encode(serStr, "UTF-8");//编码后字符串不是乱码（不编也不影响功能）

        objectOutputStream.close();
        byteArrayOutputStream.close();
        return serStr;

    };

    public static Object UnserializeStringToObject(String encoded) throws Exception{

        String redStr = URLDecoder.decode(encoded, "UTF-8");
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(redStr.getBytes("ISO-8859-1"));
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        Object obj = objectInputStream.readObject();
        objectInputStream.close();
        byteArrayInputStream.close();
        return obj;

    };

    public static void main(String[] args) {
        for (int i = 0; i <100 ; i++) {
            String time = new Date().getTime()+"";
            System.out.println(time.substring(5,time.length()));
        }
    }

}