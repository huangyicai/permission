package com.mmall.util;


import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.PutObjectResult;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class AliyunOOSUtil {
    public static void main(String[] args) {
        // Endpoint以杭州为例，其它Region请按实际情况填写。
        String endpoint = "http://oss-cn-hangzhou.aliyuncs.com";
// 云账号AccessKey有所有API访问权限，建议遵循阿里云安全最佳实践，创建并使用RAM子账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建。
        String accessKeyId = "LTAIl65k9Q4A1Eiu";
        String accessKeySecret = "6rORHoxwnONgQuJdZIHGJiLBjW2jZQ";
// 创建OSSClient实例。
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
// 上传文件流。
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream("D:\\baojia.xls");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        PutObjectResult putObjectResult = ossClient.putObject("funwl", "baojia.xls", inputStream);
        putObjectResult.getETag();
// 关闭OSSClient。
        ossClient.shutdown();
    }

}
