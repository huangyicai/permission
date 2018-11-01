package com.mmall.excel.export;

import com.mmall.excel.Bill;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.OutputStream;

/**
 * 数据导出
 * @param <T>
 */
public abstract class DataSheetExecute<T> {
    /**
     * 导出数据时，将泛型T设置到row中的每个cell内
     *
     * @param row 当前row对象
     * @param personUser   泛型T对象
     */
    void execute(Row row, Bill personUser, SXSSFWorkbook workbook) {
        row.createCell(0).setCellValue(personUser.getBillName());
        row.createCell(1).setCellValue(personUser.getSweepTime());
        row.createCell(2).setCellValue(personUser.getSerialNumber());
        row.createCell(3).setCellValue(personUser.getDestination());
        row.createCell(4).setCellValue(personUser.getWeight().toString());
//        // 内容字体样式
//        Font contFont =  workbook.createFont();
//        // 加粗
//        contFont.setBold(false);
//        // 字体名称
//        contFont.setFontName("楷体");
//        // 字体大小
//        contFont.setFontHeight((short) 11);
//        // 内容样式
//        CellStyle contentStyle = workbook.createCellStyle();
//        // 设置字体css
//        contentStyle.setFont(contFont);
//        // 竖向居中
//        contentStyle.setVerticalAlignment(VerticalAlignment.CENTER);
//        // 横向居中
//        contentStyle.setAlignment(HorizontalAlignment.CENTER);
//        // 边框
//        contentStyle.setBorderBottom(BorderStyle.THIN);
//        contentStyle.setBorderLeft(BorderStyle.THIN);
//        contentStyle.setBorderRight(BorderStyle.THIN);
//        contentStyle.setBorderTop(BorderStyle.THIN);
//
//        // 自动换行
//        contentStyle.setWrapText(false);
//
//        Cell cell0 = row.createCell(0);
//        cell0.setCellStyle(contentStyle);
//        cell0.setCellValue(personUser.getBillName());
//
//        Cell cell1 = row.createCell(1);
//        cell1.setCellStyle(contentStyle);
//        cell1.setCellValue(personUser.getSweepTime());
//
//        Cell cell2 = row.createCell(2);
//        cell2.setCellStyle(contentStyle);
//        cell2.setCellValue(personUser.getSerialNumber());
//
//        Cell cell3 = row.createCell(3);
//        cell3.setCellStyle(contentStyle);
//        cell3.setCellValue(personUser.getDestination());
//
//        Cell cell4 = row.createCell(4);
//        cell4.setCellStyle(contentStyle);
//        cell4.setCellValue(personUser.getWeight().toString());
    }


    /**
     * 输出Excel，自定义实现输出位置（保存到本地，还是上传到其他位置）
     *
     * @param workbook 生成好的excel文档薄
     */
    public abstract void writeExcel(SXSSFWorkbook workbook, OutputStream outputStream) throws Exception;
}
