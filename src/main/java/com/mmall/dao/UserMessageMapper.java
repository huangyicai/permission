package com.mmall.dao;

import com.mmall.model.UserMessage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 用户消息关联表 Mapper 接口
 * </p>
 *
 * @author qty
 * @since 2018-12-07
 */
public interface UserMessageMapper extends BaseMapper<UserMessage> {

    void updateMessage(@Param("userMessageId") String userMessageId, @Param("id")Integer id);

}
