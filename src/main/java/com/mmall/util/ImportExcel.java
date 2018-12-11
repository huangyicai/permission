package com.mmall.util;


import com.mmall.model.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFFont;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;

@Slf4j
public class ImportExcel {
    /**
     *
     * @Title: WriteExcel
     * @Description: 执行导出Excel操作
     * @param
     * @return boolean
     * @throws
     */
    public void WriteExcel(List<CustomerService> list, HttpServletResponse response) throws IOException {

        String name[]={"商户名","处理人昵称","运单号","问题描述","联络人","联系电话","附件地址","接单时间","完结时间","接单耗时",
                "完结耗时","类型","类型名称","状态","备注","创建时间"};

        // 内存中只创建100个对象，写临时文件，当超过100条，就将内存中不用的对象释放。
        SXSSFWorkbook wb = new SXSSFWorkbook(100);

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

        // 工作表对象
        Sheet sheet = null;

        // 行对象
        Row nRow = null;

        // 列对象
        Cell nCell = null;

        try {

            // 总行号
            int rowNo = 0;

            // 页行号
            int pageRowNo = 0;

            for(CustomerService c:list){
                // 打印300000条后切换到下个工作表，可根据需要自行拓展，2百万，3百万...数据一样操作，只要不超过1048576就可以
                if (rowNo % 1000000 == 0) {

                    log.info("当前sheet页为:" + rowNo / 1000000 );

                    // 建立新的sheet对象
                    sheet = wb.createSheet("第" + (rowNo / 1000000 + 1) + "个工单簿");

                    // 动态指定当前的工作表
                    sheet = wb.getSheetAt(rowNo / 1000000);


                    // 每当新建了工作表就将当前工作表的行号重置为1
                    pageRowNo = 1;

                    //定义表头
                    nRow = sheet.createRow(0);
                    for (int i = 0; i <name.length; i++) {
                        Cell cell = nRow.createCell(i);
                        cell.setCellStyle(headStyle);
                        sheet.setColumnWidth(cell.getColumnIndex(),60*70);
                        cell.setCellType(CellType.STRING);
                        cell.setCellValue(name[i]);
                    }
                }
                rowNo++;

                //转化类型
                Integer typeId = c.getTypeId();
                String type="";
                if(typeId==1){
                    type="破损";
                }else if(typeId==2){
                    type="丢失";
                }else if(typeId==3){
                    type="其他";
                }

                //转化状态
                Integer status = c.getStatus();
                String sta="";
                if(status==1){
                    sta="未处理";
                }else if(status==2){
                    sta="处理中";
                }else if(status==3){
                    sta="处理完毕";
                }

                //新建行对象
                nRow = sheet.createRow(pageRowNo++);

                //赋值
                nRow.createCell(0).setCellValue(c.getUserKey());
                nRow.createCell(1).setCellValue(c.getHandleName());
                nRow.createCell(2).setCellValue(c.getWaybillNumber());
                nRow.createCell(3).setCellValue(c.getContent());
                nRow.createCell(4).setCellValue(c.getContacts());
                nRow.createCell(5).setCellValue(c.getPhone());
                nRow.createCell(6).setCellValue(c.getEnclosure());
                nRow.createCell(7).setCellValue(c.getReceiveTime());
                nRow.createCell(8).setCellValue(c.getEndTime());
                nRow.createCell(9).setCellValue(c.getReceiveTimeSolt());
                nRow.createCell(10).setCellValue(c.getEndTimeSolt());
                nRow.createCell(11).setCellValue(type);
                nRow.createCell(12).setCellValue(c.getTypeName());
                nRow.createCell(13).setCellValue(sta);
                nRow.createCell(14).setCellValue(c.getRemarks());
                nRow.createCell(15).setCellValue(c.getCreateTime());
            }
            OutputStream out = response.getOutputStream();
            byte[] b = new byte[10240];
            response.reset();
            response.setContentType("application/csv");
            response.setHeader("content-disposition", "attachment; filename="+ URLEncoder.encode("工单.xlsx", "UTF-8"));
            wb.write(out);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            wb.close();
        }
    }
}
