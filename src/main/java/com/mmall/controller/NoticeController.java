package com.mmall.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mmall.config.UserInfoConfig;
import com.mmall.model.Notice;
import com.mmall.model.PricingGroup;
import com.mmall.model.Response.Result;
import com.mmall.model.SysUserInfo;
import com.mmall.model.params.NoticeParam;
import com.mmall.service.NoticeService;
import com.mmall.util.BeanValidator;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author hyc
 * @since 2018-09-29
 */
@Api(value = "NoticeController", description = "通知管理")
@RestController
@RequestMapping("/public/notice")
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    @ApiOperation(value = "添加通知",  notes="需要Authorization")
    @PostMapping(produces = {"application/json;charest=Utf-8"})
    public Result saveNotice(@RequestBody NoticeParam notice){
        BeanValidator.check(notice);
        SysUserInfo user = UserInfoConfig.getUserInfo();
        return noticeService.saveNotice(user,notice);
    }
    @ApiOperation(value = "通知列表",  notes="需要Authorization")
    @GetMapping(value = "list",produces = {"application/json;charest=Utf-8"})
    public Result<IPage<Notice>> getAllNotices(@RequestParam(name = "page",required = false,defaultValue = "1")Integer page,
                                               @RequestParam(name = "size",required = false,defaultValue = "10")Integer size){
        SysUserInfo user = UserInfoConfig.getUserInfo();
        Page pageResult = new Page(page,size);
        return noticeService.getAllNotices(user,pageResult);
    }

    @ApiOperation(value = "删除通知",  notes="需要Authorization")
    @DeleteMapping(value ="/{noticeId}" ,produces = {"application/json;charest=Utf-8"})
    public Result<IPage<Notice>> deleteNotice(@PathVariable("noticeId") Integer noticeId){
        noticeService.removeById(noticeId);
        return Result.ok();
    }

    @ApiOperation(value = "获取首页通知",  notes="需要Authorization")
    @GetMapping(produces = {"application/json;charest=Utf-8"})
    public Result<List<Notice>> getNotices(){
        SysUserInfo user = UserInfoConfig.getUserInfo();
        return noticeService.getNotices(user);
    }

}

