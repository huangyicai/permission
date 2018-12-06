package com.mmall.service.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mmall.config.UserInfoConfig;
import com.mmall.dao.MessageMapper;
import com.mmall.model.Message;
import com.mmall.model.Response.InfoEnums;
import com.mmall.model.Response.Result;
import com.mmall.model.SysUserInfo;
import com.mmall.model.params.MessageParam;
import com.mmall.service.MessageService;
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
            for (String id:split){
                Message m=new Message();
                m.setTitle(messageParam.getTitle());
                m.setContent(messageParam.getContent());
                m.setSendId(userInfo.getId());
                m.setUserId(Integer.parseInt(id));
                this.save(m);
            }

        }else{
            return Result.error(InfoEnums.USER_IS_NULL);
        }

        return Result.ok();
    }
}
