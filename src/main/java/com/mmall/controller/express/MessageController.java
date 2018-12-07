package com.mmall.controller.express;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mmall.config.UserInfoConfig;
import com.mmall.model.Message;
import com.mmall.model.Response.InfoEnums;
import com.mmall.model.Response.Result;
import com.mmall.model.SysUserInfo;
import com.mmall.model.params.MessageParam;
import com.mmall.service.MessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 消息表 前端控制器
 * </p>
 *
 * @author qty
 * @since 2018-12-06
 */
@Api(value = "MessageController", description = "消息表管理")
@RestController
@RequestMapping("/express/message")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @ApiOperation(value = "发送信息",  notes="需要Authorization")
    @PostMapping(value = "/addMessage",produces = {"application/json;charest=Utf-8"})
    public Result addMessage(@RequestBody MessageParam messageParam){
        return messageService.addMessage(messageParam);
    }

    @ApiOperation(value = "客户获取信息",  notes="需要Authorization")
    @GetMapping(value = "/getUserMessage",produces = {"application/json;charest=Utf-8"})
    public Result getUserMessage(){
        SysUserInfo userInfo = UserInfoConfig.getUserInfo();
        if(userInfo.getPlatformId()==3){
            List<Message> userId = messageService.list(new QueryWrapper<Message>().eq("user_id", userInfo.getId()));
            return Result.ok(userId);
        }
        return Result.error(InfoEnums.UNAUTHORIZATION);
    }

    @ApiOperation(value = "确认信息收到",  notes="需要Authorization")
    @PutMapping(value = "/confirmMessage/{messageId}",produces = {"application/json;charest=Utf-8"})
    public Result confirmMessage(@PathVariable(value = "messageId") Integer messageId){
        Message message=new Message();
        message.setId(messageId);
        message.setStatus(1);
        messageService.updateById(message);
        return Result.ok();
    }

    @ApiOperation(value = "老板获取信息列表",  notes="需要Authorization")
    @GetMapping(value = "/getSendMessage",produces = {"application/json;charest=Utf-8"})
    public Result getSendMessage(){
        SysUserInfo userInfo = UserInfoConfig.getUserInfo();
        if(userInfo.getPlatformId()==2){
            List<Message> userId = messageService.list(new QueryWrapper<Message>().eq("send_id", userInfo.getId()));
            return Result.ok(userId);
        }
        return Result.error(InfoEnums.UNAUTHORIZATION);
    }

    @ApiOperation(value = "删除信息",  notes="需要Authorization")
    @DeleteMapping(value = "/deleteMessage/{messageId}",produces = {"application/json;charest=Utf-8"})
    public Result deleteMessage(@PathVariable(value = "messageId") Integer messageId){
       messageService.removeById(messageId);
        return Result.ok();
    }
}

