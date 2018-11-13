package com.mmall.controller.express;

import com.mmall.config.UserInfoConfig;
import com.mmall.model.Response.Result;
import com.mmall.model.SysUserInfo;
import com.mmall.model.params.PricingGroupParam;
import com.mmall.model.params.UserInfoOperateParam;
import com.mmall.service.ExpressUserService;
import com.mmall.service.PricingGroupService;
import com.mmall.util.BeanValidator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * <p>
 *  个人中心
 * </p>
 *
 * @author hyc
 * @since 2018-09-28
 */
@Api(value = "UserCenterController", description = "个人中心")
@RestController
@RequestMapping("/express/userCenter")
public class UserCenterController {

    @Autowired
    private ExpressUserService expressUserService;
    @Autowired
    private PricingGroupService pricingGroupService;

    @ApiOperation(value = "快递公司注册(1=运营号,2=客服)",  notes="需要Authorization")
    @PostMapping(value = "/register/{level}",produces = {"application/json;charest=Utf-8"})
    public Result registerOperate(@RequestBody UserInfoOperateParam user,
                                  @PathVariable("level") Integer level){
        BeanValidator.check(user);
        SysUserInfo parent = UserInfoConfig.getUserInfo();
        return expressUserService.registerOperate(user,level,parent);
    }
    @ApiOperation(value = "获取快递公司=运营号/客服号)",  notes="需要Authorization")
    @GetMapping(produces = {"application/json;charest=Utf-8"})
    public Result getAllOperate(){
        SysUserInfo userInfo = UserInfoConfig.getUserInfo();
        return expressUserService.getAllOperate(userInfo);
    }

    @ApiOperation(value = "添加/修改成本定价",  notes="需要Authorization")
    @PostMapping(value = "/{cityId}",produces = {"application/json;charest=Utf-8"})
    public Result savePricingGroup(@RequestBody List<PricingGroupParam> pricingGroups,
                                   @PathVariable() Integer cityId){
        for(PricingGroupParam pgp :pricingGroups){
            BeanValidator.check(pgp);
        }
        SysUserInfo userInfo = UserInfoConfig.getUserInfo();
        return pricingGroupService.savePricingGroup(pricingGroups,userInfo.getId(),cityId);
    }


}
