package com.mmall.controller.fn;

import com.mmall.model.Response.Result;
import com.mmall.model.SysUserInfo;
import com.mmall.model.params.UserInfoExpressParm;
import com.mmall.model.params.UserInfoServiceParm;
import com.mmall.service.SysUserService;
import com.mmall.util.BeanValidator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Api(value = "FnUserController", description = "fn用户")
@RestController
@RequestMapping("/fn")
@Slf4j
public class FnUserController {
    @Autowired
    private SysUserService sysUserService;

    @ApiOperation(value = "弗恩注册快递公司",  notes="需要Authorization")
    @PostMapping(value = "/register",produces = {"application/json;charest=Utf-8"})
    public Result fnRegister(@RequestBody UserInfoExpressParm user){

        BeanValidator.check(user);
        SysUserInfo parent = (SysUserInfo)SecurityUtils.getSubject().getSession().getAttribute("user");
        return sysUserService.fnRegister(user,parent);
    }

    @ApiOperation(value = "修改快递公司信息",  notes="需要Authorization")
    @PutMapping(value = "/express",produces = {"application/json;charest=Utf-8"})
    public Result updateExpress(@RequestBody UserInfoServiceParm user){
        BeanValidator.check(user);
        return sysUserService.updateExpress(user);
    }

    @ApiOperation(value = "冻结/解冻、快递公司账号",  notes="需要Authorization")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "id",value = "用户id",dataType = "long",paramType = "path")
    )
    @PostMapping(value = "/user/frozen/{id}",produces = {"application/json;charest=Utf-8"})
    public Result frozen(@PathVariable("id") Integer id){
        return sysUserService.frozen(id);
    }

    @ApiOperation(value = "删除快递公司账号",  notes="需要Authorization")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "id",value = "用户id",dataType = "long",paramType = "path")
    )
    @DeleteMapping(value = "/user/{id}",produces = {"application/json;charest=Utf-8"})
    public Result deleteUser(@PathVariable("id") Integer id){
        return sysUserService.deleteUser(id);
    }


    @ApiOperation(value = "获取快递公司列表",  notes="需要Authorization")
    @GetMapping(value = "/companys",produces = {"application/json;charest=Utf-8"})
    public Result<List<SysUserInfo>> getCompanys(){
        SysUserInfo user = (SysUserInfo)SecurityUtils.getSubject().getSession().getAttribute("user");
        return sysUserService.getCompanys(user);
    }
}
