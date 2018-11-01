package com.mmall.config.alipay;

import java.io.FileWriter;
import java.io.IOException;

/**
 * @author Huang YiCai
 * @create 2018/10/29  13:02
 */
public class AlipayConfig {

//↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

    // 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
    public static String app_id = "2017121600869195";

    // 商户私钥，您的PKCS8格式RSA2私钥
    public static String merchant_private_key =
            "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDAapvpsa7aI7fE95NSbXwW0mhlD3ordDK7iqrcXvwBmOQIRc/fW7WbEz9uZ7/L5tTRemKDy0qzOfUQJJ8NlyXiQBVmCRKWaFNfrub95SmmKyjghOaDLO9SenA3i7Cpb7WUXv2FAIKcZgDGOcYxSYV/uYuLe8qlgEp8Ljw6PQoUhVzBdYmaZVqHDtGCT12eMiBVvMRy/mEnKiXa+Q5bdT0M4+EzG7vVh96+nZvhXsShiD+a3GHq57NCapfRZueqggBwNN5o/JfYJZ1cJL/TumnHJkl9QQa43dfT1VQZpMHK7GyiZF5zWsSOyYulGAIOOKr6OqkjL8uoXH+lBDqP99hTAgMBAAECggEAK20kKRcP9m2PQSX7GuRY2DG+m+hlUz9TX95I95l8WMPv2yoeYzOohgpPC5TgxZ0gNPLhHwuXnWGcPHt3ZjLRuwFpkvDDQCzlfDfWLEE3KCiffiSgfcR7curtPqwJQARVP9o8b3MRB0lCLXrGj0MfnRb8NovGzZlptiMdNqumBcE7YmoNhUeZ44dXrCqc8/cDjxjFGxj+asYb/hM0wY9H7dQ3qY6u26HHfY5uW0JqfyMjc0QNRuGOsoE026hvqlCbthRAsk7mi3SMqknvQtXXQkgHDduUA8JZgRaoxa1fS5Lyigaet1fMVif8ywQ6qfhprDYE4eOdfnpeFw+arpxhiQKBgQDd92Vvphl1jsXGhBDy5hkGbxACfIEn6hK2g3j1HjJA7OAbEHylLYx3BMqEervMlO3Iw1X7Vw3GEY+VfuNcAQNmdVMCX5ohnOKJO5MkcqKG3zgBAWncmSU1GEEB1R2rrELLxPV/MSruyrlSweWerrc7uq6CHpIIU/7pqcQv9Ef99QKBgQDd61Iqna+egenCiKZHb4/BKXXEZ9GtRjiQ7t8EgjK1AVwsf2Ko4m/VMSnSidg6tZpvztPpvMUT8SWmxCtSbe7/fuycjyV9Km/rzYP1eHCWWfMkDYJteujrgSmjaxHX/FHXVUl5b/CZCJRVbZKOU9+YZTtgDIRvXiEr9q+p/f6IJwKBgQChvMVTB4sBmBkQv+bBt1yAzaiyxxothWhBSad3pJslp/Tcg7TvfzMc/oa65du2BMcHRR5/2D0XGHCxlBYDUlGx9MeZsSPUXkbLjHd5VXaMJFbglLEYpxbvi07DERFfqFO7uzbPiXopKdrELLkuWUz23t6NvczW+K2EgcmMz8ktiQKBgQCIM6ivbS5fS37MHZBoYalKNYcJL2r6PRH0qhcIrlwuyZHn4ZsM6kUXBOCVTnL+vXz2yGs5ltkiPywLqXSpV72K5DyWbijlJEmi4PTvkKcRME3RP9VgGkKgYBrNKKa/+CXG0pqQxMpXMnPP5rs/TWz7HI6Pib8ylwQm9nWkELbKmwKBgF5HRXjesiGqhC/fBqOCyDMDk4ipAeymMaWgLmrYAYuZSFgvhqWZuso33mF5ThgI4lyl9YPUBaU3lKuVOwEgBcJRZN5V/b9As8wav9QscvKT/GVz+yrKTqNVQ6U3obdB9xIgArRH5C0SCa/JNbcXXgRMfF/FMHJNiY6A9nAEuxtz";

    // 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    public static String alipay_public_key =   "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAjUTwImF1ORoFJ7QJ1qVUyCm0sj3rl2Cyd6ojc83G/Ye8tREaV3QxOCTdfhkYwsf4WmfFu30JZZpfcuUzWRq61YT/3z/yeCLwJpjb4iVvumerDKSK87go7G5LuMOdtHQN3mo3USBlqxWE3w9cCVe+PJN+JYyhcPc7nGe59/o5HWOvVXV7l0Oz3jtwe1lAkZCgqe7Jcr+BhklxvgiH5xQIR44XA9oigKxiekSdYPIjwVK7nyTC/fC9T+XNjuI5G71qoXPr6d/GIDlai7fT1oo8/fB0AWrHdR4lbbGJrTNb92LGD8gjiUivq9233K/LB/81dlTVxthK0OAKtAgIJ5mTfwIDAQAB";

    // 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String notify_url = "http://www.funwl.com/public/alipay/notify";

    // 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String return_url = "http://www.funwl.com/#/kdCustomerServices";

    // 签名方式
    public static String sign_type = "RSA2";

    // 字符编码格式
    public static String charset = "utf-8";

    // 支付宝网关
    public static String gatewayUrl = "https://openapi.alipay.com/gateway.do";

    // 支付宝网关
    public static String log_path = "C:\\";


//↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

    /**
     * 写日志，方便测试（看网站需求，也可以改成把记录存入数据库）
     * @param sWord 要写入日志里的文本内容
     */
    public static void logResult(String sWord) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(log_path + "alipay_log_" + System.currentTimeMillis()+".txt");
            writer.write(sWord);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
