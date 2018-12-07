package com.mmall.service.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mmall.dao.CustomerUserMapper;
import com.mmall.model.CustomerUser;
import com.mmall.model.Response.Result;
import com.mmall.service.CustomerUserService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 客服绑定客户表 服务实现类
 * </p>
 *
 * @author qty
 * @since 2018-12-06
 */
@Service
public class CustomerUserServiceImpl extends ServiceImpl<CustomerUserMapper, CustomerUser> implements CustomerUserService {

}
