package com.mmall.service.serviceImpl;

import com.mmall.model.UserMessage;
import com.mmall.dao.UserMessageMapper;
import com.mmall.service.UserMessageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户消息关联表 服务实现类
 * </p>
 *
 * @author qty
 * @since 2018-12-07
 */
@Service
public class UserMessageServiceImpl extends ServiceImpl<UserMessageMapper, UserMessage> implements UserMessageService {

}
