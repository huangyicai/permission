package com.mmall.service;

import com.mmall.model.Message;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mmall.model.Response.Result;
import com.mmall.model.params.MessageParam;

/**
 * <p>
 * 消息表 服务类
 * </p>
 *
 * @author qty
 * @since 2018-12-06
 */
public interface MessageService extends IService<Message> {
    Result addMessage(MessageParam messageParam);
}
