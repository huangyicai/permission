package com.mmall.util;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mmall.component.ApplicationContextHelper;
import com.mmall.dao.SysUserInfoMapper;
import com.mmall.dao.SysUserMapper;
import com.mmall.model.SysUser;
import com.mmall.model.SysUserInfo;
import com.mmall.model.params.UserInfoExpressParm;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.poi.ss.usermodel.CellType.STRING;

/**
 * 读取excel(用户模板)
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
    public Map<String,List> readExcel(MultipartFile xlsxFile) throws IOException {
        if (xlsxFile == null || "".equals(xlsxFile)) {
            return new HashMap<>();
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
    public Map<String,List> readXlsx(MultipartFile xlsxFile) throws IOException {
        InputStream inputStream = xlsxFile.getInputStream();
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(inputStream);

        UserInfoExpressParm uiep = null;
        List<UserInfoExpressParm> list = new ArrayList<UserInfoExpressParm>();
        List<String> listError = new ArrayList<String>();

        SysUserMapper sysUserMapper = ApplicationContextHelper.getBeanClass(SysUserMapper.class);
        SysUserInfoMapper sysUserInfoMapper = ApplicationContextHelper.getBeanClass(SysUserInfoMapper.class);
        for (int numSheet = 0; numSheet < xssfWorkbook.getNumberOfSheets(); numSheet++) {
            XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(numSheet);
            if (xssfSheet == null) {
                continue;
            }

            for (int rowNum = 0; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
                XSSFRow xssfRow = xssfSheet.getRow(rowNum);

                if(rowNum==0){
                    String[] str={"账号","公司名称","昵称","联系电话","负责人","用户邮箱","省","市","区","详细地址"};
                    for (int i = 0; i <10 ; i++) {
                        String cell = check(xssfRow.getCell(i),rowNum,listError);
                        if(!str[i].equals(cell)){
                            listError.add("第一行，第"+(i+1)+"列表格格式错误");
                            break;
                        }
                    }
                }else {
                    uiep = new UserInfoExpressParm();
                    uiep.setUsername(check(xssfRow.getCell(0),rowNum,listError));

                    SysUser userByusername =sysUserMapper.selectOne(new QueryWrapper<SysUser>().eq("username",uiep.getUsername()));
                    if(userByusername!=null){
                        SysUserInfo sysUserInfo = sysUserInfoMapper.selectOne(new QueryWrapper<SysUserInfo>().eq("user_id", userByusername.getId()).in("status", -1,0, 1));
                        if(sysUserInfo!=null){
                            listError.add("第"+(rowNum+1)+"行用户已经存在");
                            continue;
                        }
                    }

                    uiep.setPassword("123456");
                    uiep.setCompanyName(check(xssfRow.getCell(1),rowNum,listError));
                    uiep.setName( check(xssfRow.getCell(2),rowNum,listError));
                    XSSFCell cell = xssfRow.getCell(3);
                    cell.setCellType(STRING);
                    uiep.setTelephone(cell.toString());
                    uiep.setPersonInCharge(check(xssfRow.getCell(4),rowNum,listError));
                    uiep.setEmail( check(xssfRow.getCell(5),rowNum,listError));
                    uiep.setProvince(check(xssfRow.getCell(6),rowNum,listError));
                    uiep.setCity(check(xssfRow.getCell(7),rowNum,listError));
                    uiep.setArea( check(xssfRow.getCell(8),rowNum,listError));
                    uiep.setAddress(check(xssfRow.getCell(9),rowNum,listError));
                    list.add(uiep);
                }
            }
        }
        Map<String,List> map=new HashMap<>();
        map.put("list",list);
        map.put("listError",listError);
        return map;
    }

    /**
     * 校验数据
     * @return
     */
    public String check(XSSFCell xssfCell,Integer rowNum,List<String> listError){
        String s="";
        if(xssfCell!=null){
            s = xssfCell.toString().replaceAll("\u00A0", "");
        }

        if("".equals(s)){
            listError.add("第"+(rowNum+1)+"行有数据为空");
        }
        return s;
    }
}
