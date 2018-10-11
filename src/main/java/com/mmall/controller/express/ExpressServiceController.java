package com.mmall.controller.express;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mmall.config.UserInfoConfig;
import com.mmall.model.CustomerService;
import com.mmall.model.Response.Result;
import com.mmall.model.SysUserInfo;
import com.mmall.service.CustomerServiceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(value = "ExpressServiceController", description = "客服/工单")
@RestController
@RequestMapping("/express/service")
@Slf4j
public class ExpressServiceController {
    @Autowired
    private CustomerServiceService customerServiceService;


    @ApiOperation(value = "获取所有工单（0=全部，1=未处理，2=处理中，3=处理完毕）",  notes="需要Authorization")
    @GetMapping(value = "/list/{status}",produces = {"application/json;charest=Utf-8"})
    public Result getAllCustomerService(@PathVariable("status") Integer status,
                                        @RequestParam(name = "waybillNumber",required = false) String waybillNumber,
                                        @RequestParam(name = "createTime",required = false) String createTime,
                                        @RequestParam(name = "endTime",required = false) String endTime,
                                        @RequestParam(name = "page",required = false,defaultValue = "1")Integer page,
                                        @RequestParam(name = "size",required = false,defaultValue = "10")Integer size){
        SysUserInfo userInfo = UserInfoConfig.getUserInfo();
        Page ipage = new Page(page,size);
        return customerServiceService.getAllCustomerService(status,userInfo.getId(),ipage,waybillNumber,createTime,endTime);
    }

    @ApiOperation(value = "我处理的工单（0=全部，2=处理中，3=处理完毕）",  notes="需要Authorization")
    @GetMapping(value = "/self/list/{status}",produces = {"application/json;charest=Utf-8"})
    public Result getCustomerServiceBySelf(@PathVariable("status") Integer status,
                                        @RequestParam(name = "waybillNumber",required = false) String waybillNumber,
                                        @RequestParam(name = "page",required = false,defaultValue = "1")Integer page,
                                        @RequestParam(name = "size",required = false,defaultValue = "10")Integer size){
        SysUserInfo userInfo = UserInfoConfig.getUserInfo();
        Page ipage = new Page(page,size);
        return customerServiceService.getCustomerServiceBySelf(status,userInfo.getId(),ipage,waybillNumber);
    }

    @ApiOperation(value = "我来处理工单",  notes="需要Authorization")
    @PostMapping(value = "/handle/{handleId}",produces = {"application/json;charest=Utf-8"})
    public Result handleService(@PathVariable("handleId") Integer handleId){
        SysUserInfo userInfo = UserInfoConfig.getUserInfo();
        return customerServiceService.handleService(handleId,userInfo);
    }
    @ApiOperation(value = "转至其他客服处理",  notes="需要Authorization")
    @PostMapping(value = "/forward/{handleId}/{userId}",produces = {"application/json;charest=Utf-8"})
    public Result forwardhandleService(@PathVariable("handleId") Integer handleId,
                                       @PathVariable("userId") Integer userId){
        CustomerService byId = customerServiceService.getById(handleId);
        byId.setUserId(userId);
        customerServiceService.save(byId);
        return Result.ok();
    }

    @ApiOperation(value = "处理完毕",  notes="需要Authorization")
    @GetMapping(value = "/finish/{handleId}",produces = {"application/json;charest=Utf-8"})
    public Result handleFinishService(@PathVariable("handleId") Integer handleId,
                                      @RequestParam(name = "remarks",required = true) String remarks){
        CustomerService byId = customerServiceService.getById(handleId);
        byId.setRemarks(remarks);
        byId.setStatus(3);
        customerServiceService.save(byId);
        return Result.ok();
    }

}
