package com.mmall.controller;


import com.aliyuncs.exceptions.ClientException;
import com.fasterxml.jackson.annotation.JsonView;
import com.mmall.dto.SysMenuDto;
import com.mmall.model.Response.Result;
import com.mmall.model.*;
import com.mmall.model.params.SysUserParam;
import com.mmall.model.params.UserPasswordParam;
import com.mmall.service.SysUserService;
import com.mmall.util.BeanValidator;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 *
 *
 * @author hyc
 * @since 2018-09-15
 */
@Api(value = "SysUserController", description = "用户管理")
@RestController
@RequestMapping("/public")
@Slf4j
public class SysUserController {
    @Autowired
    private SysUserService sysUserService;

    @ApiOperation(value = "获取菜单",  notes="不需要Authorization")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "platId",value = "平台ID",required = false,dataType = "long",paramType = "query")
    )
    @GetMapping(value = "/menus",produces = {"application/json;charest=Utf-8"})
    @JsonView(Result.ResultMenus.class)
    public  Result<List<SysMenuDto>> findAllMenuByUser(@RequestParam(name = "platId",required = false,defaultValue = "0") Integer platId){
        SysUserInfo user = (SysUserInfo)SecurityUtils.getSubject().getSession().getAttribute("user");
        return sysUserService.findAllMenuByUser(user,platId);
    }

    @ApiOperation(value = "获取个人信息",  notes="需要Authorization")
    @GetMapping(value = "/user",produces = {"application/json;charest=Utf-8"})
    public Result<Map<String,Object>> getUserInfo(){
        SysUserInfo user = (SysUserInfo) SecurityUtils.getSubject().getSession().getAttribute("user");
        return sysUserService.getUserInfo(user);
    }
    @ApiOperation(value = "修改密码",  notes="需要Authorization")
    @PutMapping(value = "/user/password/{code}",produces = {"application/json;charest=Utf-8"})
    public Result updateUserPassword(@RequestBody UserPasswordParam userPasswordParam,
                                                         @PathVariable("code") String code){
        SysUserInfo user = (SysUserInfo) SecurityUtils.getSubject().getSession().getAttribute("user");
        return sysUserService.updateUserPassword(user,code,userPasswordParam);
    }


    @ApiOperation(value = "获取验证码", httpMethod = "POST")
    @PostMapping(value = "/getCode/{phone}",produces = {"application/json;charest=Utf-8"})
    public Result getCode(@PathVariable("phone") String phone) throws ClientException {
        return sysUserService.getCode(phone);

    }

    @ApiOperation(value = "登录",  notes="不需要Authorization")
    @PostMapping(value = "/login",produces = {"application/json;charest=Utf-8"})
    public Result<AuthInfo<SysUserInfo>> login(@RequestBody SysUserParam user){
        BeanValidator.check(user);
        return sysUserService.login(user.getUsername(),user.getPassword());
    }
    @ApiOperation(value = "退出登录",  notes="需要Authorization")
    @GetMapping(value = "/logout",produces = {"application/json;charest=Utf-8"})
    public Result logout(){
        Subject subject = SecurityUtils.getSubject();
        String s = (String)subject.getPrincipal();
        log.info(s);
        subject.logout();
        Serializable id = subject.getSession().getId();
        return Result.ok();
    }
    @GetMapping(value = "/403",produces = {"application/json;charest=Utf-8"})
    public Map<String ,String> unAuthenticated(){
        throw new UnauthenticatedException();
    }



}

