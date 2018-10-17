package com.mmall.controller.customer;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mmall.model.Response.Result;
import com.mmall.model.SysUserInfo;
import com.mmall.model.Total;
import com.mmall.model.params.BillDetailsParam;
import com.mmall.model.params.CredentialsParam;
import com.mmall.service.TotalService;
import com.mmall.vo.TotalVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "CustomerTotalController", description = "账单")
@RestController
@RequestMapping("/customer/total")
@Slf4j
public class CustomerTotalController {

    @Autowired
    private TotalService totalService;

    @ApiOperation(value = "获取未付款账单提示",  notes="需要Authorization")
    @GetMapping(value = "/getNotPaying",produces = {"application/json;charest=Utf-8"})
    public Result getNotPaying(){
        SysUserInfo user = (SysUserInfo) SecurityUtils.getSubject().getSession().getAttribute("user");
        List<Total> list = totalService.list(new QueryWrapper<Total>()
                .eq(user.getId() != null, "user_id", user.getId()).select("name").eq("total_state",2));
        return Result.ok(list);
    }

    @ApiOperation(value = "获取账单列表",  notes="需要Authorization")
    @PostMapping(value = "/getBill")
    public Result<IPage<TotalVo>> getBill(@RequestBody BillDetailsParam billDetailsParam){
        IPage<TotalVo> page=new Page<>(billDetailsParam.getCurrent(),billDetailsParam.getSize());
        IPage<TotalVo> bill = totalService.getBill(page,billDetailsParam,2);
        return Result.ok(bill);
    }

    @ApiOperation(value = "上传凭证",  notes="需要Authorization")
    @PostMapping(value = "/setCredentials")
    public Result<IPage<TotalVo>> setCredentials(@RequestBody CredentialsParam credentialsParam){
        Total total=new Total();
        total.setTotalId(credentialsParam.getTotalId());
        total.setTotalState(3);
        total.setTotalCredentialsUrl(credentialsParam.getTotalCredentialsUrl());
        totalService.updateById(total);
        return Result.ok();
    }
}
