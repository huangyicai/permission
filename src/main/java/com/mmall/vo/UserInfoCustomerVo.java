package com.mmall.vo;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jmessage.api.JMessageClient;
import cn.jmessage.api.message.MessageListResult;
import cn.jmessage.api.user.UserInfoResult;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserInfoCustomerVo {
    public static void main(String[] args) {
        JMessageClient client = new JMessageClient("e4a1e8f0b36855ee35669700", "21c1c75b99ade674a7b44aff");
        try {
            //UserInfoResult test_user = client.getUserInfo("root");
            //MessageListResult messageList = client.getMessageList(5, "2018-09-28 10:10:10", "2018-09-30 18:10:12");
            MessageListResult messageList = client.getMessageListByCursor("275C63A64CBD9EE1A7235E977EA0A44C");
            //log.info(test_user.toString());
            log.info(messageList.toString());
        } catch (APIConnectionException e) {
            log.error("Connection error. Should retry later. ", e);
        } catch (APIRequestException e) {
            log.info("Error response from JPush server. Should review and fix it. ", e);
            log.info("HTTP Status: " + e.getStatus());
            log.info("Error Message: " + e.getMessage());
        }
    }
}
