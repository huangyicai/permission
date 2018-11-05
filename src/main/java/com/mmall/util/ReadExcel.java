package com.mmall.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.mmall.model.params.UserInfoExpressParm;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

/**
 * 读取excel
 * @author qty
 * @created 2018-11-5
 */
public class ReadExcel{

    /**
     * 判断
     * @param xlsxFile
     * @return
     * @throws IOException
     */
    public List<UserInfoExpressParm> readExcel(MultipartFile xlsxFile) throws IOException {
        if (xlsxFile == null || "".equals(xlsxFile)) {
            return new ArrayList<UserInfoExpressParm>();
        } else {
            return readXlsx(xlsxFile);
        }
    }

    /**
     * 读取数据
     * @param xlsxFile
     * @return
     * @throws IOException
     */
    public List<UserInfoExpressParm> readXlsx(MultipartFile xlsxFile) throws IOException {
        InputStream inputStream = xlsxFile.getInputStream();
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(inputStream);
        UserInfoExpressParm uiep = null;
        List<UserInfoExpressParm> list = new ArrayList<UserInfoExpressParm>();

        for (int numSheet = 0; numSheet < xssfWorkbook.getNumberOfSheets(); numSheet++) {
            XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(numSheet);
            if (xssfSheet == null) {
                continue;
            }

            for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
                XSSFRow xssfRow = xssfSheet.getRow(rowNum);
                if (xssfRow != null) {
                    uiep = new UserInfoExpressParm();
                    uiep.setUsername(xssfRow.getCell(0).toString());
                    uiep.setPassword("123456");
                    uiep.setCompanyName(xssfRow.getCell(1).toString());
                    uiep.setName(xssfRow.getCell(2).toString());
                    uiep.setTelephone(xssfRow.getCell(3).toString());
                    uiep.setPersonInCharge(xssfRow.getCell(4).toString());
                    uiep.setEmail(xssfRow.getCell(5).toString());
                    uiep.setProvince(xssfRow.getCell(6).toString());
                    uiep.setCity(xssfRow.getCell(7).toString());
                    uiep.setArea(xssfRow.getCell(8).toString());
                    uiep.setAddress(xssfRow.getCell(9).toString());
                    list.add(uiep);
                }
            }
        }
        return list;
    }
}
