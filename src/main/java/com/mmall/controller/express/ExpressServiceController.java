package com.mmall.controller.express;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonView;
import com.mmall.Socket.ExpressWebSocket;
import com.mmall.config.UserInfoConfig;
import com.mmall.dao.SysUserInfoMapper;
import com.mmall.dto.SysUserInfoDto;
import com.mmall.model.CustomerService;
import com.mmall.model.Response.Result;
import com.mmall.model.SysUserInfo;
import com.mmall.model.params.WorkReplyParam;
import com.mmall.service.CustomerServiceService;
import com.mmall.util.BeanValidator;
import com.mmall.util.DateTimeUtil;
import com.mmall.util.ImportExcel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Api(value = "ExpressServiceController", description = "客服/工单")
@RestController
@RequestMapping("/express/service")
@Slf4j
public class ExpressServiceController {
    @Autowired
    private CustomerServiceService customerServiceService;
    @Autowired
    private SysUserInfoMapper sysUserInfoMapper;

    @ApiOperation(value = "获取所有工单（0=全部，1=未处理，2=处理中，3=处理完毕）",  notes="需要Authorization")
    @GetMapping(value = "/list",produces = {"application/json;charest=Utf-8"})
    public Result getAllCustomerService(@RequestParam(name="status",required = false,defaultValue = "0") Integer status,
                                        @RequestParam(name = "type",required = false,defaultValue = "0") Integer type,
                                        @RequestParam(name = "waybillNumber",required = false) String waybillNumber,
                                        @RequestParam(name = "keyName",required = false) String keyName,
                                        @RequestParam(name = "accountUserList",required = false) String handleIds,
                                        @RequestParam(name = "createTime",required = false) String createTime,
                                        @RequestParam(name = "endTime",required = false) String endTime,
                                        @RequestParam(name = "receiveSolt",required = false,defaultValue = "-1") Integer receiveSolt,
                                        @RequestParam(name = "endSolt",required = false,defaultValue = "-1") Integer endSolt,
                                        @RequestParam(name = "page",required = false,defaultValue = "1")Integer page,
                                        @RequestParam(name = "size",required = false,defaultValue = "10")Integer size){
        SysUserInfo userInfo = UserInfoConfig.getUserInfo();
        Page ipage = new Page(page,size);
        return customerServiceService.getAllCustomerService(status,type,userInfo.getId(),ipage,waybillNumber,keyName,createTime,endTime,receiveSolt,endSolt,handleIds);
    }

    @ApiOperation(value = "我处理的工单（0=全部，2=处理中，3=处理完毕）",  notes="需要Authorization")
    @GetMapping(value = "/self/list",produces = {"application/json;charest=Utf-8"})
    public Result getCustomerServiceBySelf(@RequestParam(name="status",required = false,defaultValue = "0") Integer status,
                                           @RequestParam(name = "type",required = false,defaultValue = "0") Integer type,
                                           @RequestParam(name = "keyName",required = false) String keyName,
                                        @RequestParam(name = "waybillNumber",required = false) String waybillNumber,
                                           @RequestParam(name = "createTime",required = false) String createTime,
                                           @RequestParam(name = "endTime",required = false) String endTime,
                                        @RequestParam(name = "page",required = false,defaultValue = "1")Integer page,
                                        @RequestParam(name = "size",required = false,defaultValue = "10")Integer size){
        //SysUserInfo userInfo = UserInfoConfig.getUserInfo();
        SysUserInfo userInfo = (SysUserInfo) SecurityUtils.getSubject().getSession().getAttribute("user");
        Page ipage = new Page(page,size);
        return customerServiceService.getCustomerServiceBySelf(status,type,userInfo.getId(),ipage,waybillNumber,createTime,endTime,keyName);
    }

    @ApiOperation(value = "我来处理工单",  notes="需要Authorization")
    @PostMapping(value = "/handle/{handleId}",produces = {"application/json;charest=Utf-8"})
    public Result handleService(@PathVariable("handleId") Integer handleId){
        SysUserInfo userInfo = (SysUserInfo) SecurityUtils.getSubject().getSession().getAttribute("user");
        return customerServiceService.handleService(handleId,userInfo);
    }
    @ApiOperation(value = "转至其他客服处理",  notes="需要Authorization")
    @PostMapping(value = "/forward/{handleId}/{userId}",produces = {"application/json;charest=Utf-8"})
    public Result forwardhandleService(@PathVariable("handleId") String handleId,
                                       @PathVariable("userId") Integer userId){
        return customerServiceService.updateCustomerService(userId,handleId);
    }

    @ApiOperation(value = "获取其他客服",  notes="需要Authorization")
    @GetMapping(value = "/customers",produces = {"application/json;charest=Utf-8"})
    @JsonView(SysUserInfoDto.UserInfoView.class)
    public Result getCustomers(){
        SysUserInfo userInfo = UserInfoConfig.getUserInfo();
        List<SysUserInfo> sysUserInfos = sysUserInfoMapper.selectList(new QueryWrapper<SysUserInfo>()
                .eq("parent_id", userInfo.getId())
                .eq("platform_id", userInfo.getPlatformId())
                .eq("display", 0)
                .in("status",0,1));
        return Result.ok(sysUserInfos);
    }

    @ApiOperation(value = "处理完毕",  notes="需要Authorization")
    @GetMapping(value = "/finish/{handleId}",produces = {"application/json;charest=Utf-8"})
    public Result handleFinishService(@PathVariable("handleId") Integer handleId,
                                      @RequestParam(name = "remarks",required = true) String remarks){
        //当前时间
        long currentTime = System.currentTimeMillis();
        CustomerService byId = customerServiceService.getById(handleId);
        byId.setRemarks(remarks);
        byId.setEndTime(DateTimeUtil.numToDate(currentTime,"yyyy-MM-dd HH:mm:ss"));
        byId.setStatus(3);
        //工单创建时间
        long createT = DateTimeUtil.DateToNum(byId.getCreateTime());
        byId.setEndTimeSolt((currentTime-createT)/(1000*60));
        customerServiceService.saveOrUpdate(byId);

        SysUserInfo sysUserInfo = sysUserInfoMapper.selectById(byId.getUserId());
        ExpressWebSocket.sendMsgAddServices(sysUserInfo,"已处理完毕，请查看","运单号:"+ byId.getWaybillNumber(),5);
        return Result.ok(byId);
    }

    @ApiOperation(value = "回复工单",  notes="需要Authorization")
    @PostMapping(value = "/reply/{handleId}",produces = {"application/json;charest=Utf-8"})
    public Result saveReply(@PathVariable("handleId") Integer handleId,
                        @RequestBody WorkReplyParam workReplyParam){
        BeanValidator.check(workReplyParam);
        SysUserInfo user = (SysUserInfo) SecurityUtils.getSubject().getSession().getAttribute("user");
        return customerServiceService.reply(user.getId(),handleId,workReplyParam.getContent());
    }

    @ApiOperation(value = "获取工单回复记录",  notes="需要Authorization")
    @GetMapping(value = "/reply/{handleId}",produces = {"application/json;charest=Utf-8"})
    public Result getReplys(@PathVariable("handleId") Integer handleId,
                            @RequestParam(name = "page",required = false,defaultValue = "1")Integer page,
                            @RequestParam(name = "size",required = false,defaultValue = "10")Integer size){
        return customerServiceService.getReplys(page,size,handleId);
    }

    @ApiOperation(value = "总的工单统计",  notes="需要Authorization")
    @GetMapping(value = "/reply/all",produces = {"application/json;charest=Utf-8"})
    public Result getAllReplys(@RequestParam(name="dateBegin",required = false)String dateBegin,
                               @RequestParam(name="dateEnd",required = false)String dateEnd){
        SysUserInfo user = UserInfoConfig.getUserInfo();
        return customerServiceService.getAllReplys(user,dateBegin,dateEnd);
    }

    @ApiOperation(value = "每个客服工单统计",  notes="需要Authorization")
    @GetMapping(value = "/reply/byService",produces = {"application/json;charest=Utf-8"})
    public Result getAllReplysByService(@RequestParam(name="dateBegin",required = false)String dateBegin,
                                        @RequestParam(name="dateEnd",required = false)String dateEnd){
        SysUserInfo user = UserInfoConfig.getUserInfo();
        return customerServiceService.getAllReplysByService(user,dateBegin,dateEnd);
    }

    @ApiOperation(value = "获取该快递公司所有快递账号",  notes="需要Authorization")
    @GetMapping(value = "/getAccounts",produces = {"application/json;charest=Utf-8"})
    public Result getAccounts(){
        SysUserInfo user = UserInfoConfig.getUserInfo();
        return customerServiceService.getAccounts(user);
    }

    @ApiOperation(value = "获取未处理的工单数",  notes="需要Authorization")
    @GetMapping(value = "/noHandle",produces = {"application/json;charest=Utf-8"})
    public Result getAllByNoHandle(){
        SysUserInfo user = UserInfoConfig.getUserInfo();
        return customerServiceService.getAllByNoHandle(user);
    }

    @ApiOperation(value = "导出所有工单（0=全部，1=未处理，2=处理中，3=处理完毕）",  notes="需要Authorization")
    @GetMapping(value = "/ExportOrder",produces = {"application/json;charest=Utf-8"})
    public void ExportOrder(@RequestParam(name="status",required = false,defaultValue = "0") Integer status,
                            @RequestParam(name="token",required = false) String token,
                              @RequestParam(name = "type",required = false,defaultValue = "0") Integer type,
                              @RequestParam(name = "waybillNumber",required = false) String waybillNumber,
                              @RequestParam(name = "keyName",required = false) String keyName,
                            @RequestParam(name = "accountUserList",required = false) String handleIds,
                              @RequestParam(name = "createTime",required = false) String createTime,
                              @RequestParam(name = "endTime",required = false) String endTime,
                              @RequestParam(name = "receiveSolt",required = false,defaultValue = "-1") Integer receiveSolt,
                              @RequestParam(name = "endSolt",required = false,defaultValue = "-1") Integer endSolt,
                            HttpServletResponse response) throws IOException {
        SysUserInfo userInfo = UserInfoConfig.getUserInfo();
        List<CustomerService> allCustomerService = customerServiceService.getAllCustomerService(status, type, userInfo.getId(), waybillNumber, keyName, createTime, endTime, receiveSolt, endSolt,handleIds);
        ImportExcel ie=new ImportExcel();
        ie.WriteExcel(allCustomerService, response);
    }
}
