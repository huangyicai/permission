package com.mmall.controller.express;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mmall.config.UserInfoConfig;
import com.mmall.dao.MessageMapper;
import com.mmall.model.Response.InfoEnums;
import com.mmall.model.Response.Result;
import com.mmall.model.SysUserInfo;
import com.mmall.model.UserMessage;
import com.mmall.model.params.MessageParam;
import com.mmall.service.MessageService;
import com.mmall.service.UserMessageService;
import com.mmall.vo.MessVO;
import com.mmall.vo.MessageVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
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

    @Autowired
    private UserMessageService userMessageService;

    @Autowired
    private MessageMapper messageMapper;

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
            List<MessageVo> listByIds = messageMapper.ListByIds(userInfo.getId().toString());
            return Result.ok(listByIds);
        }
        return Result.error(InfoEnums.UNAUTHORIZATION);
    }

    @ApiOperation(value = "确认信息收到",  notes="需要Authorization")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userMessageId",value = "用户信息关联id",dataType = "long",paramType = "path")
    })
    @PutMapping(value = "/confirmMessage/{userMessageId}",produces = {"application/json;charest=Utf-8"})
    public Result confirmMessage(@PathVariable(value = "userMessageId") Integer userMessageId){
        UserMessage message=new UserMessage();
        message.setStatus(1);
        message.setId(userMessageId);
        userMessageService.updateById(message);
        return Result.ok();
    }

    @ApiOperation(value = "老板获取信息列表",  notes="需要Authorization")
    @GetMapping(value = "/getSendMessage",produces = {"application/json;charest=Utf-8"})
    public Result getSendMessage(){
        SysUserInfo userInfo = UserInfoConfig.getUserInfo();
        if(userInfo.getPlatformId()==2){
            List<MessVO> listByIds = messageMapper.getListByIds(userInfo.getId().toString());
            return Result.ok(listByIds);
        }
        return Result.error(InfoEnums.UNAUTHORIZATION);
    }

    @ApiOperation(value = "删除信息",  notes="需要Authorization")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userMessageId",value = "用户信息关联id",dataType = "long",paramType = "path")
    })
    @DeleteMapping(value = "/deleteMessage/{userMessageId}",produces = {"application/json;charest=Utf-8"})
    public Result deleteMessage(@PathVariable(value = "userMessageId") Integer userMessageId){
        UserMessage byId = userMessageService.getById(userMessageId);
        List<UserMessage> messageId = userMessageService.list(new QueryWrapper<UserMessage>().eq("message_id", byId.getMessageId()));
        if(messageId==null || messageId.size()==0){
            messageMapper.deleteById( byId.getMessageId());
        }
        userMessageService.removeById(userMessageId);
       return Result.ok();
    }
}

