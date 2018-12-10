package com.mmall.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mmall.dao.CustomerUserMapper;
import com.mmall.model.CustomerUser;
import com.mmall.model.Response.InfoEnums;
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

    /**
     * 批量添加客户
     * @param userId
     * @param customerUserId
     * @return
     */
    @Override
    public Result addCustomerUser(String userId, Integer customerUserId) {
        if("".equals(userId) || userId==null){
            return Result.error(InfoEnums.USER_IS_NULL);
        }

        String[] split = userId.split(",");

        for(String str:split){
            CustomerUser one = this.getOne(new QueryWrapper<CustomerUser>()
                    .eq("user_id", Integer.parseInt(str)));
            if(one!=null){
                return Result.error(InfoEnums.USER_EXIST);
            }

            CustomerUser cu=new CustomerUser();
            cu.setCustomerId(customerUserId);
            cu.setUserId(Integer.parseInt(str));
            this.save(cu);
        }
        return Result.ok();
    }

    /**
     * 获取客服下的客户
     * @param customerUserId
     * @return
     */
    @Override
    public Result getCustomerUserList(Integer customerUserId) {
        return null;
    }
}
