package com.mmall.dao;

import com.mmall.model.FnContacts;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 弗恩快递负责人 Mapper 接口
 * </p>
 *
 * @author hyc
 * @since 2018-11-07
 */
public interface FnContactsMapper extends BaseMapper<FnContacts> {

    FnContacts getOneFnContacts(@Param("id") Integer id);
}
