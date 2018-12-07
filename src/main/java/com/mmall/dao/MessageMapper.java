package com.mmall.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mmall.model.Message;
import com.mmall.vo.MessVO;
import com.mmall.vo.MessageVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 消息表 Mapper 接口
 * </p>
 *
 * @author qty
 * @since 2018-12-06
 */
public interface MessageMapper extends BaseMapper<Message> {

    List<MessageVo> ListByIds(@Param("idStr") String idStr);

    List<MessVO> getListByIds(@Param("idStr") String idStr);
}
