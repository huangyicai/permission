package com.mmall.controller.express;

import com.fasterxml.jackson.annotation.JsonView;
import com.mmall.dto.SysUserInfoDto;
import com.mmall.model.BillKeyword;
import com.mmall.model.Response.Result;
import com.mmall.model.SysUserInfo;
import com.mmall.model.params.UserInfoExpressParm;
import com.mmall.service.ExpressUserService;
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

import java.util.List;

@Api(value = "ExpressUserController", description = "快递用户管理")
@RestController
@RequestMapping("/express")
@Slf4j
public class ExpressUserController {

    @Autowired
    private ExpressUserService expressUserService;

    @ApiOperation(value = "快递公司注册(1=客户,2=分支)",  notes="需要Authorization")
    @PostMapping(value = "/register/{id}/{level}",produces = {"application/json;charest=Utf-8"})
    public Result expressRegister(@RequestBody UserInfoExpressParm user,
                                  @PathVariable("id") Integer id,
                                  @PathVariable("level") Integer level){
        SysUserInfo parent = (SysUserInfo) SecurityUtils.getSubject().getSession().getAttribute("user");
        return expressUserService.expressRegister(user,parent,id,level);
    }

    @ApiOperation(value = "获取用户信息",  notes="需要Authorization")
    @GetMapping(value = "/cusmoters/{id}",produces = {"application/json;charest=Utf-8"})
    @JsonView(SysUserInfoDto.UserInfoView.class)
    public Result<SysUserInfo> getCusmotersInfo(@PathVariable Integer id){
        return expressUserService.getCusmotersInfo(id);
    }
    @ApiOperation(value = "获取用户列表",  notes="需要Authorization")
    @GetMapping(value = "/cusmoters",produces = {"application/json;charest=Utf-8"})
    @JsonView(SysUserInfoDto.UserInfoView.class)
    public Result<List<SysUserInfoDto>> getCusmoters(){
        SysUserInfo user = (SysUserInfo)SecurityUtils.getSubject().getSession().getAttribute("user");
        return expressUserService.getCusmoters(user);
    }


    @ApiOperation(value = "冻结/解冻、客户账号",  notes="需要Authorization")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "id",value = "用户id",dataType = "long",paramType = "path")
    )
    @PostMapping(value = "/user/frozen/{id}",produces = {"application/json;charest=Utf-8"})
    public Result frozen(@PathVariable("id") Integer id){
        return expressUserService.frozen(id);
    }

    @ApiOperation(value = "删除客户账号",  notes="需要Authorization")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "id",value = "用户id",dataType = "long",paramType = "path")
    )
    @DeleteMapping(value = "/user/{id}",produces = {"application/json;charest=Utf-8"})
    public Result deleteUser(@PathVariable("id") Integer id){
        return expressUserService.deleteUser(id);
    }

    @ApiOperation(value = "添加客户商户名（用于账单）",  notes="需要Authorization")
    @PostMapping(value = "/keyword",produces = {"application/json;charest=Utf-8"})
    public Result saveKeyword(@RequestBody BillKeyword billKeywords){
        return expressUserService.saveKeyword(billKeywords);
    }

    @ApiOperation(value = "获取客户商户名",  notes="需要Authorization")
    @GetMapping(value = "/keyword/{userId}",produces = {"application/json;charest=Utf-8"})
    public Result<List<BillKeyword>> findAllKeywordById(@PathVariable Integer userId){
        return expressUserService.findAllKeywordById(userId);
    }

    @ApiOperation(value = "删除客户商户名",  notes="需要Authorization")
    @DeleteMapping(value = "/keyword/{keyId}",produces = {"application/json;charest=Utf-8"})
    public Result deleteKeywordBykeyId(@PathVariable Integer keyId){
        return expressUserService.deleteKeywordBykeyId(keyId);
    }


}
