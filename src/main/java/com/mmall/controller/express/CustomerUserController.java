package com.mmall.controller.express;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mmall.model.CustomerUser;
import com.mmall.model.Response.Result;
import com.mmall.model.SysUserInfo;
import com.mmall.service.CustomerUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 客服绑定客户表 前端控制器
 * </p>
 *
 * @author qty
 * @since 2018-12-07
 */
@Api(value = "CustomerUserController", description = "客服绑定客户表管理")
@RestController
@RequestMapping("/express/customerUser")
public class CustomerUserController {

    @Autowired
    private CustomerUserService customerUserService;

    @ApiOperation(value = "获取客服下的客户",notes = "需要Authorization")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "customerUserId",value = "客服id",dataType = "long",paramType = "path")
    })
    @GetMapping(value = "/getCustomerUserList/{customerUserId}",produces = {"application/json;charest=Utf-8"})
    public Result getCustomerUserList(@PathVariable Integer customerUserId){
        return customerUserService.getCustomerUserList(customerUserId);
    }
    @ApiOperation(value = "获取分支下未绑定的客户", notes="需要Authorization")
    @GetMapping(value = "/branchUsers/{id}",produces = {"application/json;charest=Utf-8"})
    public Result<List<SysUserInfo>> getBranchCusmotersUser(@PathVariable("id")Integer id){
        return customerUserService.getBranchCusmotersUserNotbinding(id);
    }
    @ApiOperation(value = "客服绑定客户",notes = "需要Authorization")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId",value = "客户id",dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "customerUserId",value = "客服id",dataType = "long",paramType = "path")
    })
    @PostMapping(value = "/addCustomerUser/{userId}/{customerUserId}",produces = {"application/json;charest=Utf-8"})
    public Result addCustomerUser( @PathVariable("userId")  String userId,
                                  @PathVariable("customerUserId") Integer customerUserId){
        return customerUserService.addCustomerUser(userId,customerUserId);
    }

    @ApiOperation(value = "客服解绑客户",notes = "需要Authorization")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "customerUserId",value = "客服id",dataType = "Stirng",paramType = "query")
    })
    @DeleteMapping(value = "/deleteCustomerUser/{customerUserId}",produces = {"application/json;charest=Utf-8"})
    public Result deleteCustomerUser(@PathVariable("customerUserId")Integer customerUserId){

        customerUserService.remove(new QueryWrapper<CustomerUser>().eq("user_id",customerUserId));
        return Result.ok();
    }

    @ApiOperation(value = "客服修改绑定客户",notes = "需要Authorization")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "customerId",value = "新客服id",dataType = "long",paramType = "path"),
            @ApiImplicitParam(name = "customerUserId",value = "客服绑定客户id",dataType = "long",paramType = "path")
    })
    @PutMapping(value = "/updateCustomerUser/{customerUserId}/{customerId}",produces = {"application/json;charest=Utf-8"})
    public Result updateCustomerUser(@PathVariable Integer customerId,
                                     @PathVariable Integer customerUserId){
        CustomerUser cu=new CustomerUser();
        cu.setCustomerId(customerId);
        cu.setId(customerUserId);
        customerUserService.updateById(cu);
        return Result.ok();
    }

}

