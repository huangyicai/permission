package com.mmall.controller.express;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.mmall.config.UserInfoConfig;
import com.mmall.dao.MessageMapper;
import com.mmall.dao.SysUserInfoMapper;
import com.mmall.dao.UserMessageMapper;
import com.mmall.dto.MessageDto;
import com.mmall.model.Message;
import com.mmall.model.Response.InfoEnums;
import com.mmall.model.Response.Result;
import com.mmall.model.SysUserInfo;
import com.mmall.model.UserMessage;
import com.mmall.model.params.MessageParam;
import com.mmall.service.MessageService;
import com.mmall.service.UserMessageService;
import com.mmall.vo.MessVO;
import com.mmall.vo.MessageVo;
import com.mmall.vo.UserMessageVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
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
    private UserMessageMapper userMessageMapper;
    @Autowired
    private MessageMapper messageMapper;
    @Autowired
    private SysUserInfoMapper sysUserInfoMapper;

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
            List<Message> listByIds = messageMapper.ListByIds(userInfo.getId());
            return Result.ok(listByIds);
        }
        return Result.error(InfoEnums.UNAUTHORIZATION);
    }

    @ApiOperation(value = "确认信息收到",  notes="需要Authorization")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userMessageId",value = "用户信息关联id",dataType = "long",paramType = "path")
    })
    @PutMapping(value = "/confirmMessage/{userMessageId}",produces = {"application/json;charest=Utf-8"})
    public Result confirmMessage(@PathVariable(value = "userMessageId") String userMessageId){
        SysUserInfo userInfo = UserInfoConfig.getUserInfo();
        userMessageMapper.updateMessage(userMessageId,userInfo.getId());
        return Result.ok();
    }

    @ApiOperation(value = "老板获取信息列表",  notes="需要Authorization")
    @GetMapping(value = "/getSendMessage",produces = {"application/json;charest=Utf-8"})
    public Result getSendMessage(){
        SysUserInfo userInfo = UserInfoConfig.getUserInfo();
        if(userInfo.getPlatformId()==2){
            List<Message> listByIds = messageMapper.selectList(new QueryWrapper<Message>()
                    .eq("send_id",userInfo.getId()));

            List<MessageDto> messageDtos = Lists.newArrayList();

            for(Message message : listByIds){
                MessageDto adapt = MessageDto.adapt(message);
                List<UserMessage> message_id = userMessageMapper.selectList(new QueryWrapper<UserMessage>()
                        .eq("message_id", message.getId()));
                for(UserMessage userMessage:message_id){
                    UserMessageVo uv = new UserMessageVo();
                    uv.setName(sysUserInfoMapper.findUserName(userMessage.getUserId()));
                    uv.setState(userMessage.getStatus());
                    adapt.getUserMessageVo().add(uv);
                }
                messageDtos.add(adapt);
            }
            return Result.ok(messageDtos);
        }
        return Result.error(InfoEnums.UNAUTHORIZATION);
    }

    @ApiOperation(value = "删除信息",  notes="需要Authorization")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userMessageId",value = "用户信息关联id",dataType = "long",paramType = "path")
    })
    @DeleteMapping(value = "/deleteMessage/{userMessageId}",produces = {"application/json;charest=Utf-8"})
    @Transactional
    public Result deleteMessage(@PathVariable(value = "userMessageId") Integer userMessageId){
        userMessageMapper.delete(new QueryWrapper<UserMessage>().eq("message_id",userMessageId));
        messageMapper.delete(new QueryWrapper<Message>().eq("id",userMessageId));
        return Result.ok();
    }
}

