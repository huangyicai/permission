package com.mmall.util;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;
import com.mmall.model.Response.InfoEnums;
import com.mmall.model.Response.Result;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URL;

/**
 * @author qty
 */

@Component
public class UploadApi {

    private static String endpoint="https://oss-cn-hangzhou.aliyuncs.com/";

    private static String accessKeyId="LTAIKUD6G9dChBet";

    private static String accessKeySecret="GAHME0xpODyBuARYpQW2U0bwaEzgzo";

    private static String bucketName="funwl";

    private static String returnBaseUrl="https://funwl.oss-cn-hangzhou.aliyuncs.com/";

    /**
     * 文件上传
     * @param file
     */
    public static String upload(File file) {

        String filename = RandomHelper.generateRandomStr(5) + file.getName();
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        ObjectMetadata objectMetadata = new ObjectMetadata();
        // 设置content type
        String strContentType = contentType(filename.substring(filename.lastIndexOf(".")));
        objectMetadata.setContentType(strContentType);
        try {
            if (!ossClient.doesBucketExist(bucketName)) {
                ossClient.createBucket(bucketName);
            }
//            ossClient.putObject (bucketName, getFileName(file), file, objectMetadata);
            ossClient.putObject (bucketName, getFileName(file), file);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ossClient.shutdown();
        }
        return returnBaseUrl + getFileName(file);
    }

    /**
     * 本地文件上传
     * @param file
     * @param firstKey 文件名
     * @param ossPath OSS仓库中的地址
     * @return
     */
    public static String upload(File file, String firstKey, String ossPath) {

        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        ObjectMetadata objectMetadata = new ObjectMetadata();
        // 设置content type
        String strContentType;
        if (firstKey.lastIndexOf(".")>0){
            strContentType = contentType(firstKey.substring(firstKey.lastIndexOf(".")));
        }else {
            strContentType = contentType(firstKey);
        }

        objectMetadata.setContentType(strContentType);
        try {
            if (!ossClient.doesBucketExist(bucketName)) {
                ossClient.createBucket(bucketName);
            }
//            ossClient.putObject(bucketName, ossPath+firstKey, file, objectMetadata);
            ossClient.putObject(bucketName, ossPath+firstKey, file);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ossClient.shutdown();
        }
        return returnBaseUrl +  ossPath+firstKey;
    }

    /**
     * 字符串方式上传
     * @param imgBase64
     * @param key
     * @return
     */
    
    public String uploadOSS(String imgBase64, String key) {
        if (StringUtils.isEmpty(imgBase64)) {
            return null;
        }
        // 创建OSSClient实例
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        try {
//            byte[] bytes = Base64.decodeBase64(imgBase64);
        	byte[] bytes = Base64.decodeBase64(imgBase64);
////            log.debug("imgString 明文:[{}{}]",imgBase64.length(),imgBase64);
////            log.debug("imgByte 明文:[{},{}]",bytes.length, bytes);
//            // 把base64字符串存入OSS
//        	 InputStream input = new ByteArrayInputStream(bytes);
//             BufferedImage image = ImageIO.read(input);
//             if (image == null) {
//                 System.out.println("图片转码错误");
//                 return "";
//             }
//             ByteArrayOutputStream baos = new ByteArrayOutputStream();
//             ImageIO.write(image, "jpg", baos);
//            byte[] new_bytes = baos.toByteArray();
            // 上传文件
            ossClient.putObject(bucketName, key, new ByteArrayInputStream(bytes));
        } catch (OSSException oe) {
            oe.printStackTrace();
        } catch (ClientException ce) {
            ce.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭client
            ossClient.shutdown();
        }
        return returnBaseUrl +  key;
    }
    
    /**
     * 流式上传
     *
     * @param inputStream
     * @param firstKey
     */
    public String upload(InputStream inputStream, String firstKey) {
        firstKey = RandomHelper.generateRandomStr(5) + firstKey;
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        ObjectMetadata objectMetadata = new ObjectMetadata();
        // 设置content type
        String strContentType = contentType(firstKey.substring(firstKey.lastIndexOf(".")));
        objectMetadata.setContentType(strContentType);
        try {
            if (!ossClient.doesBucketExist(bucketName)) {
                ossClient.createBucket(bucketName);
            }
            ossClient.putObject(bucketName, firstKey, inputStream, objectMetadata);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ossClient.shutdown();
        }
        return returnBaseUrl + firstKey;
    }

    public InputStream download(OSSClient ossClient, String filename) {// 创建OSSClient实例

            OSSObject ossObject = ossClient.getObject(bucketName, filename);

            // 读Object内容
            System.out.println("Object content:");
//        BufferedReader reader = new BufferedReader(new InputStreamReader(ossObject.getObjectContent()));
//        while (true) {
//            String line = reader.readLine();
//            if (line == null) {break;}
//            System.out.println("\n" + line);
//        }
//        reader.close();
            //外部一定要close，否则造成资源泄露
            return ossObject.getObjectContent();


    }
    
	public String downloadPicByURL(String urlStr) {
		URL url;
		BufferedImage image;
		String imgBase64 = "";
		try {
			url = new URL(urlStr);
			image = ImageIO.read(url);
			if (image == null) {
				System.out.println("图片转码错误");
				return null;
			}
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(image, "jpg", baos);
			baos.flush();
			byte[] new_bytes = baos.toByteArray();
//				logger.debug("new_bytes:[{},{}]",new_bytes.length,new_bytes);
//			InputStream inputStreams = new ByteArrayInputStream(new_bytes);
//				logger.debug("读取后的base64字符串:[{}]",imgBase64);
			imgBase64 = Base64.encodeBase64String(new_bytes);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return imgBase64;
	}
    
    /**
     * 获取文件名
     *
     * @param file
     * @return
     */
    private static String getFileName(File file) {
        String fName = file.getName();
        fName = fName.trim();
        String temp[] = fName.split("\\\\");
        fName = temp[temp.length - 1];
        return fName;
    }

    /**
     * Description: 判断OSS服务文件上传时文件的contentType
     *
     * @param FilenameExtension 文件后缀
     * @return String
     * @Version1.0
     */
    public static String contentType(String FilenameExtension) {
        if (FilenameExtension.contains("BMP") || FilenameExtension.contains("bmp")) {
            return "image/bmp";
        }
        if (FilenameExtension.contains("GIF") || FilenameExtension.contains("gif")) {
            return "image/gif";
        }
        if (FilenameExtension.contains("JPEG") || FilenameExtension.contains("jpeg") ||
                FilenameExtension.contains("JPG") || FilenameExtension.contains("jpg") ||
                FilenameExtension.contains("PNG") || FilenameExtension.contains("png")) {
            return "image/jpeg";
        }
        if (FilenameExtension.contains("HTML") || FilenameExtension.contains("html")) {
            return "text/html";
        }
        if (FilenameExtension.contains("TXT") || FilenameExtension.contains("txt")) {
            return "text/plain";
        }
        if (FilenameExtension.contains("VSD") || FilenameExtension.contains("vsd")) {
            return "application/vnd.visio";
        }
        if (FilenameExtension.contains("PPTX") || FilenameExtension.contains("pptx") ||
                FilenameExtension.contains("PPT") || FilenameExtension.contains("ppt")) {
            return "application/vnd.ms-powerpoint";
        }
        if (FilenameExtension.contains("DOCX") || FilenameExtension.contains("docx") ||
                FilenameExtension.contains("DOC") || FilenameExtension.contains("doc")) {
            return "application/msword";
        }
        if (FilenameExtension.contains("XML") || FilenameExtension.contains("xml")) {
            return "text/xml";
        }
        if (FilenameExtension.contains("PDF") || FilenameExtension.contains("pdf")) {
            return "application/pdf";
        }
        return "text/html";
    }

    /**
     * 测试是否能够成功上传
     * @param args
     */
    public static void main(String[] args) {
        String path="E:\\GDW\\0459634.xlsx";
        File file=new File(path);
        System.out.println(upload(file,"123.xlsx","01/张三/"));
    }
}
