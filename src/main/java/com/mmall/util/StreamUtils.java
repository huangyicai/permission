package com.mmall.util;

import com.mmall.excel.Bill;

import java.math.BigDecimal;
import java.util.*;
import java.io.*;
import java.net.*;


public class StreamUtils{

    public static void main(String[] args) throws Exception {

        //制造数据
        List<Bill> packagesArray = new ArrayList<Bill>();
        for (int i = 0; i < 100000; i++) {
            packagesArray.add(new Bill("金XX111"
                    , "2018-9-20" + i
                    , "男:" + i
                    , "137****2152:" + i
                    ,  new BigDecimal(i)));
        }


        // 将一个对象序列化为一个字符串
        String str = SerializeObjectToString(packagesArray);

        // 保存到文件 saveToFile(str);

        // 从文件读取字符串 str = readFromFile()

        // 将一个字符串反序化为一个对象
        packagesArray = (List<Bill>) UnserializeStringToObject(str);

        System.out.println(packagesArray);
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

}