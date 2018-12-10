package com.mmall.dto;

import com.google.common.collect.Lists;
import com.mmall.model.CourierCompany;
import com.mmall.model.Message;
import com.mmall.model.SysUserInfo;
import com.mmall.model.UserMessage;
import com.mmall.vo.UserMessageVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * @author Huang YiCai
 * @create 2018/12/07  18:46
 */
@Getter
@Setter
@ToString
public class MessageDto extends Message{

    private List<UserMessageVo> userMessageVo = Lists.newArrayList();

    public static MessageDto adapt(Message message) {
        MessageDto dto = new MessageDto();
        BeanUtils.copyProperties(message, dto);
        return dto;
    }
}
