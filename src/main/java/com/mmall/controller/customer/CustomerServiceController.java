package com.mmall.controller.customer;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mmall.config.UserInfoConfig;
import com.mmall.dao.HandleTypeMapper;
import com.mmall.dto.ReplynumServiceDto;
import com.mmall.model.CustomerService;
import com.mmall.model.HandleType;
import com.mmall.model.Response.Result;
import com.mmall.model.SysUserInfo;
import com.mmall.model.params.CustomerServiceParam;
import com.mmall.service.CustomerServiceService;
import com.mmall.util.BeanValidator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "CustomerServiceController", description = "客服/工单")
@RestController
@RequestMapping("/customer/service")
@Slf4j
public class CustomerServiceController {


    @Autowired
    private CustomerServiceService customerServiceService;

    @ApiOperation(value = "添加工单",  notes="需要Authorization")
    @PostMapping(produces = {"application/json;charest=Utf-8"})
    public Result saveCustomerService(@RequestBody CustomerServiceParam customerServiceParam){
        BeanValidator.check(customerServiceParam);
        SysUserInfo userInfo = UserInfoConfig.getUserInfo();
        return customerServiceService.saveCustomerService(userInfo,customerServiceParam);
    }



    @ApiOperation(value = "获取工单（0=全部，1=未处理，2=处理中，3=处理完毕）",  notes="需要Authorization")
    @GetMapping(value = "/list/{status}",produces = {"application/json;charest=Utf-8"})
    public Result<Page<ReplynumServiceDto>> getAllCustomerService(@PathVariable("status") Integer status,
                                                                  @RequestParam(name = "waybillNumber",required = false) String waybillNumber,
                                                                  @RequestParam(name = "page",required = false,defaultValue = "1")Integer page,
                                                                  @RequestParam(name = "size",required = false,defaultValue = "10")Integer size){
        SysUserInfo userInfo = UserInfoConfig.getUserInfo();
        Page ipage = new Page(page,size);
        return customerServiceService.getAllCustomerServiceByUser(status,userInfo.getId(),ipage,waybillNumber);
    }
    @ApiOperation(value = "删除工单",  notes="需要Authorization")
    @DeleteMapping(value = "/{id}",produces = {"application/json;charest=Utf-8"})
    public Result deleteCustomerService(@PathVariable("id") Integer id){
        customerServiceService.removeById(id);
        return Result.ok();
    }
}
