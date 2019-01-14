package com.mmall.excel.export;

import com.mmall.excel.Bill;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.OutputStream;
import java.math.BigDecimal;

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
        int i = personUser.getOffer().compareTo(new BigDecimal(0));
        if(i!=0){
            row.createCell(5).setCellValue(personUser.getOffer().doubleValue());
        }
    }


    /**
     * 输出Excel，自定义实现输出位置（保存到本地，还是上传到其他位置）
     *
     * @param workbook 生成好的excel文档薄
     */
    public abstract void writeExcel(SXSSFWorkbook workbook, OutputStream outputStream) throws Exception;
}
