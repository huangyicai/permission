package com.mmall.dao;

import com.mmall.model.CustomerUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mmall.model.SysUserInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 客服绑定客户表 Mapper 接口
 * </p>
 *
 * @author qty
 * @since 2018-12-07
 */
public interface CustomerUserMapper extends BaseMapper<CustomerUser> {
    List<SysUserInfo> getCustomerUserList(@Param("customerUserId") String customerUserId);
}
