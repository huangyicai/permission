package com.mmall.excel.thread;

import com.mmall.excel.Bill;
import com.mmall.excel.export.DataSheetExecute;
import com.mmall.excel.export.ExcelExportExecutor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.concurrent.Callable;

public class ThreadImport implements Callable<String> {

    private String path;

    private List<Bill> list;

    private String key;

    public ThreadImport(String path, List<Bill> list,String key) {
        this.path = path;
        this.list = list;
        this.key=key;
    }

    /**
     * 线程执行导出
     * @return
     * @throws Exception
     */
    public String call() throws Exception {
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



        new ExcelExportExecutor<Bill>(strings, list, dataSheetExecute, true).execute();


        return path;
    }
}
