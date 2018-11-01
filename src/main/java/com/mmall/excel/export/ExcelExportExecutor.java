package com.mmall.excel.export;

import com.mmall.excel.Bill;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import static org.apache.poi.ss.usermodel.HorizontalAlignment.CENTER;

/**
 * 导出执行器
 * @param <T>
 */
public class ExcelExportExecutor<T> {
    /**
     * 工作簿
     */
    private SXSSFWorkbook wb = new SXSSFWorkbook(100);
    /**
     * 表格
     */
    private Sheet sheet;
    /**
     * 原始数据list
     */
    private List<Bill> data;
    /**
     * 分批次写入数据，分页条数
     */
    private int pageSize =5;
    /**
     * 当前输出的列
     */
    private int currentRow = 0;
    /**
     * 数据行起始行数
     */
    private int dataRowStart = 1;
    /**
     * 表头
     */
    private String[] rowHeader;
    /**
     * 是否开启row状态监听
     */
    private boolean watcherRowStatus;


    ExcelExportExecutor(String[] rowHeader, List<Bill> data) {
        this();
        this.rowHeader = rowHeader;
        this.data = data;
    }

    ExcelExportExecutor(String[] rowHeader, List<Bill> data, DataSheetExecute<T> executorListener) {
        this(rowHeader, data);
        this.executorListener = executorListener;
    }

    public ExcelExportExecutor(String[] rowHeader
            , List<Bill> data
            , DataSheetExecute<T> executorListener
            , boolean watcherRowStatus
            ) {

        this(rowHeader, data, executorListener);
        this.watcherRowStatus = watcherRowStatus;
    }


    ExcelExportExecutor() {
        SXSSFSheet sheet = this.wb.createSheet();
        this.sheet = sheet;
    }

    /**
     * 数据切割时的数据设置执行器
     */
    private DataSheetExecute<T> executorListener;

    public void setExecutorListener(DataSheetExecute<T> executorListener) {
        this.executorListener = executorListener;
    }


    public void execute() {
        // 字体样式
        XSSFFont xssfFont = (XSSFFont) wb.createFont();
        // 加粗
        xssfFont.setBold(true);
        // 字体名称
        xssfFont.setFontName("楷体");
        // 字体大小
        xssfFont.setFontHeight(12);

        // 表头样式
        CellStyle headStyle =  wb.createCellStyle();
        // 设置字体css
        headStyle.setFont(xssfFont);
        // 竖向居中
        headStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        // 横向居中
        headStyle.setAlignment(HorizontalAlignment.CENTER);
        // 边框
        headStyle.setBorderBottom(BorderStyle.THIN);
        headStyle.setBorderLeft(BorderStyle.THIN);
        headStyle.setBorderRight(BorderStyle.THIN);
        headStyle.setBorderTop(BorderStyle.THIN);

        List<Bill> data = this.data;
        pageSize=data.size();
        /**
         * 处理表头
         */
        if (this.executorListener == null) {
            throw new RuntimeException("未设置执行处理器");
        }
        /**
         * 设置表头
         */
        Row row = this.sheet.createRow(this.dataRowStart - 1);
        Integer[] width={60*70,60*70,60*80,60*120,60*50,60*50,60*50};
        for (int i = 0; i < rowHeader.length; i++) {
            Cell cell=row.createCell(i);
            cell.setCellStyle(headStyle);
            sheet.setColumnWidth(cell.getColumnIndex(), width[i]);
            cell.setCellValue(rowHeader[i]);
        }
        if (data == null) {
            throw new RuntimeException("无效的数据");
        }

        int size = data.size();

        /**
         * 导出总条数小于分页条数时，则直接导入，不用分页导入
         */
        if (size < pageSize) {
            Row tmpRow;
            for (int i = 0; i < size; i++) {
                tmpRow = sheet.createRow(this.dataRowStart + i);
                Cell cell=tmpRow.createCell(i);
//                cell.setCellStyle(style);
                this.executorListener.execute(tmpRow, data.get(i),wb);
            }
//            data.clear();
        } else if (pageSize > 0 && size >= pageSize) {
            /*
             * 数据总条数
             */
            int listSize = data.size();
            /*
             * 分批次导入数据次数
             */
            int batchSize = listSize / pageSize;
            /*
             * 分批次后，剩余的记录条数
             */
            int remain = listSize % pageSize;
            List<Bill> tmp;

            for (int i = 0; i < batchSize; i++) {
                /*
                 * 此处快被搞疯了，list.subList(from,to)方法太坑，截取到的是list的视图。
                 * 如果tmp list在后边clear的话，就会导致data数据丢失，data的长度会逐渐减少pageSize个。
                 * 导致suList方法会报异常。于是就将代码改为subList(0,pageSize)。
                 */

                /*
                 * 将原始list数据按照pageSize分批截取
                 */
                tmp = data.subList(0, pageSize);
                int len = tmp.size();
                Row tmpRow;
                for (int j = 0; j < len; j++) {
                    /*
                     * 记录当前执行到哪一行了
                     */
                    currentRow = i * pageSize + j + 1;
                    /*
                     * 创建一个row
                     */
                    tmpRow = sheet.createRow(currentRow);
//                    if (watcherRowStatus && executorListener != null) {
//                        executorListener.listen(tmpRow, currentRow);
//                    }
                    /*
                     * 将list中的数据对象设置到指定的row中
                     */
                    this.executorListener.execute(tmpRow, tmp.get(j),wb);
                }
                /**
                 * 清除缓存list，优化内存
                 */
//                tmp.clear();
            }
            if (remain > 0) {
                tmp = data.subList(0, remain);
                int len = tmp.size();
                Row tmpRow;
                for (int j = 0; j < len; j++) {
                    tmpRow = sheet.createRow(currentRow + j + 1);
                    this.executorListener.execute(tmpRow, tmp.get(j),wb);
                }
                /**
                 * 清除缓存list，优化内存
                 */
//                tmp.clear();
            }
        }
        OutputStream outputStream = null;

        try {
            executorListener.writeExcel(this.wb, outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            /*
             * 确保自定义保存后忘记调用dispose
             */
            if (this.wb != null) {
                this.wb.dispose();
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    System.out.println("关闭输出流失败");
                }

            }
        }
    }


}
