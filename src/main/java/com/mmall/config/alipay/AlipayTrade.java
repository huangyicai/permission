package com.mmall.config.alipay;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.*;
import com.alipay.api.response.*;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Huang YiCai
 * @create 2018/10/29  12:22
 */
public class AlipayTrade {

    private static AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AlipayConfig.app_id, AlipayConfig.merchant_private_key, "json", AlipayConfig.charset, AlipayConfig.alipay_public_key, AlipayConfig.sign_type);

    /**
     * 交易关闭
     * @param outTradeNo 唯一订单号
     * @return
     * @throws AlipayApiException
     */
    public static AlipayTradeCloseResponse close(String outTradeNo) throws AlipayApiException {

        //设置请求参数
        AlipayTradeCloseRequest alipayRequest = new AlipayTradeCloseRequest();
        alipayRequest.setBizContent("{\"out_trade_no\":\""+ outTradeNo +"\"}");
        //请求
        AlipayTradeCloseResponse execute = alipayClient.execute(alipayRequest);
        return execute;
    }

    /**
     * 付款
     * @param totalAmount 付款金额
     */
    public static AlipayTradePagePayResponse pay(String totalAmount,String outTradeNo) throws AlipayApiException {
        //设置请求参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(AlipayConfig.return_url);
        alipayRequest.setNotifyUrl(AlipayConfig.notify_url);

        //商户订单号，商户网站订单系统中唯一订单号，必填
        //String out_trade_no = "blbl"+System.currentTimeMillis();
        //付款金额，必填
        String total_amount = totalAmount;
        //订单名称，必填
        String subject = "bailibaili订单";
        //商品描述，可空
        String body = "百里百里系统";

        alipayRequest.setBizContent("{\"out_trade_no\":\""+ outTradeNo +"\","
                + "\"total_amount\":\""+ total_amount +"\","
                + "\"subject\":\""+ subject +"\","
                + "\"body\":\""+ body +"\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");

        //请求
        AlipayTradePagePayResponse alipayTradePagePayResponse = alipayClient.pageExecute(alipayRequest);

        //输出
        return alipayTradePagePayResponse;
    }

    /**
     * 退款
     * @param outTradeNo 订单号
     * @param refundReason 退款的原因说明
     * @param amount 需要退款的金额
     * @return
     * @throws AlipayApiException
     */
    public static AlipayTradeRefundResponse refund(String outTradeNo,String refundReason,String amount) throws AlipayApiException {
        //设置请求参数
        AlipayTradeRefundRequest alipayRequest = new AlipayTradeRefundRequest();

        //标识一次退款请求，同一笔交易多次退款需要保证唯一，如需部分退款，则此参数必传
        String out_request_no = outTradeNo;

        alipayRequest.setBizContent("{\"out_trade_no\":\""+ outTradeNo +"\","
                //+ "\"trade_no\":\""+ trade_no +"\","
                + "\"refund_amount\":\""+ amount +"\","
                + "\"refund_reason\":\""+ refundReason +"\","
                + "\"out_request_no\":\""+ out_request_no +"\"}");
        //请求
        AlipayTradeRefundResponse execute = alipayClient.execute(alipayRequest);
        return execute;
    }


    /**
     * 退款查询
     * @param outTradeNo
     * @return
     * @throws AlipayApiException
     */
    public static AlipayTradeFastpayRefundQueryResponse fastpayRefundQuery(String outTradeNo) throws AlipayApiException {
        //设置请求参数
        AlipayTradeFastpayRefundQueryRequest alipayRequest = new AlipayTradeFastpayRefundQueryRequest();
        //商户订单号，商户网站订单系统中唯一订单号
        //String out_trade_no = new String(request.getParameter("WIDRQout_trade_no").getBytes("ISO-8859-1"),"UTF-8");
        //支付宝交易号
        //String trade_no = new String(request.getParameter("WIDRQtrade_no").getBytes("ISO-8859-1"),"UTF-8");
        //请二选一设置
        //请求退款接口时，传入的退款请求号，如果在退款请求时未传入，则该值为创建交易时的外部交易号，必填
        //String out_request_no = new String(request.getParameter("WIDRQout_request_no").getBytes("ISO-8859-1"),"UTF-8");

        alipayRequest.setBizContent("{\"out_trade_no\":\"" + outTradeNo + "\","
                //+"\"trade_no\":\""+ trade_no +"\","
                + "\"out_request_no\":\"" + outTradeNo + "\"}");

        //请求
        AlipayTradeFastpayRefundQueryResponse execute = alipayClient.execute(alipayRequest);

        return execute;
    }

    /**
     * 交易查询
     * @param outTradeNo
     * @return
     * @throws AlipayApiException
     */
    public static AlipayTradeQueryResponse query(String outTradeNo) throws AlipayApiException {

        AlipayTradeQueryRequest alipayRequest = new AlipayTradeQueryRequest();

        //请二选一设置

        alipayRequest.setBizContent("{\"out_trade_no\":\""+ outTradeNo +"\"}");

        //请求
        AlipayTradeQueryResponse execute = alipayClient.execute(alipayRequest);

        return execute;
    }


    /**
     * return_url
     * @return
     * @throws AlipayApiException
     */
    public static String returnUrl(HttpServletRequest request) throws AlipayApiException, UnsupportedEncodingException {

        //获取支付宝GET过来反馈信息
        Map<String,String> params = new HashMap<String,String>();
        Map<String,String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用
            valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }

        boolean signVerified = AlipaySignature.rsaCheckV1(params, AlipayConfig.alipay_public_key, AlipayConfig.charset, AlipayConfig.sign_type); //调用SDK验证签名

        //——请在这里编写您的程序（以下代码仅作参考）——
        if(signVerified) {
            //商户订单号
            String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");

            //支付宝交易号
            String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");

            //付款金额
            String total_amount = new String(request.getParameter("total_amount").getBytes("ISO-8859-1"),"UTF-8");

            return "trade_no:"+trade_no+"<br/>out_trade_no:"+out_trade_no+"<br/>total_amount:"+total_amount;
        }else {
            return "验签失败";
        }

    }

    public static Object notifyUrl(HttpServletRequest request) throws UnsupportedEncodingException, AlipayApiException {

        //获取支付宝POST过来反馈信息
        Map<String,String> params = new HashMap<String,String>();
        Map<String,String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用
            valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }

        boolean signVerified = AlipaySignature.rsaCheckV1(params, AlipayConfig.alipay_public_key, AlipayConfig.charset, AlipayConfig.sign_type); //调用SDK验证签名

        /* 实际验证过程建议商户务必添加以下校验：
        1、需要验证该通知数据中的out_trade_no是否为商户系统中创建的订单号，
        2、判断total_amount是否确实为该订单的实际金额（即商户订单创建时的金额），
        3、校验通知中的seller_id（或者seller_email) 是否为out_trade_no这笔单据的对应的操作方（有的时候，一个商户可能有多个seller_id/seller_email）
        4、验证app_id是否为该商户本身。
        */
        if(signVerified) {//验证成功
            //商户订单号
            String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");

            //支付宝交易号
            String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");

            //交易状态
            String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"),"UTF-8");

            if(trade_status.equals("TRADE_FINISHED")){
                //判断该笔订单是否在商户网站中已经做过处理
                //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                //如果有做过处理，不执行商户的业务程序

                //注意：
                //退款日期超过可退款期限后（如三个月可退款），支付宝系统发送该交易状态通知
            }else if (trade_status.equals("TRADE_SUCCESS")){
                //判断该笔订单是否在商户网站中已经做过处理
                //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                //如果有做过处理，不执行商户的业务程序

                //注意：
                //付款完成后，支付宝系统发送该交易状态通知
            }

            return "success";

        }else {
            return "fail";
        }

    }
}
