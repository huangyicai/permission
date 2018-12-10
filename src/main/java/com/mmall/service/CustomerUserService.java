package com.mmall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mmall.model.CustomerUser;
import com.mmall.model.Response.Result;

/**
 * <p>
 * 客服绑定客户表 服务类
 * </p>
 *
 * @author qty
 * @since 2018-12-07
 */
public interface CustomerUserService extends IService<CustomerUser> {

    Result addCustomerUser(String userId,Integer customerUserId);

    Result getCustomerUserList(Integer customerUserId);

}
