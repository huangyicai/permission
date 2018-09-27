package com.mmall.excel.export;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.OutputStream;

/**
 * 数据导出
 * @param <T>
 */
public interface DataSheetExecute<T> {
    /**
     * 导出数据时，将泛型T设置到row中的每个cell内
     *
     * @param row 当前row对象
     * @param t   泛型T对象
     */
    void execute(Row row, T t);

    /**
     * 输出Excel，自定义实现输出位置（保存到本地，还是上传到其他位置）
     *
     * @param workbook 生成好的excel文档薄
     */
    void writeExcel(SXSSFWorkbook workbook, OutputStream outputStream) throws Exception;

    /**
     * 导入记录条数状态监听，也可以对row对象进行格式化操作。
     */
    void listen(Row row, int rows);
}
