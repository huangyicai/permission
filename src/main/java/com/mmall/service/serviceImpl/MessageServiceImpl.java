package com.mmall.service.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mmall.config.UserInfoConfig;
import com.mmall.dao.MessageMapper;
import com.mmall.model.Message;
import com.mmall.model.Response.InfoEnums;
import com.mmall.model.Response.Result;
import com.mmall.model.SysUserInfo;
import com.mmall.model.UserMessage;
import com.mmall.model.params.MessageParam;
import com.mmall.service.MessageService;
import com.mmall.service.UserMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 消息表 服务实现类
 * </p>
 *
 * @author qty
 * @since 2018-12-06
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {

    @Autowired
    private UserMessageService userMessageService;

    /**
     * 添加信息
     * @param messageParam
     * @return
     */
    @Override
    public Result addMessage(MessageParam messageParam) {
        if(!"".equals(messageParam.getUserId()) && messageParam.getUserId()!=null){

            String[] split = messageParam.getUserId().split(",");
            SysUserInfo userInfo = UserInfoConfig.getUserInfo();

            //添加信息
            Message m=new Message();
            m.setTitle(messageParam.getTitle());
            m.setContent(messageParam.getContent());
            m.setSendId(userInfo.getId());
            this.save(m);

            //添加关联表
            for (String id:split){
                UserMessage um=new UserMessage();
                um.setMessageId(m.getId());
                um.setUserId(Integer.parseInt(id));
                userMessageService.save(um);//hyc改了
            }

        }else{
            return Result.error(InfoEnums.USER_IS_NULL);
        }

        return Result.ok();
    }
}
