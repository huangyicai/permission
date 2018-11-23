package com.mmall.controller;

/**
 * @author Huang YiCai
 * @create 2018/10/29  12:57
 */

import com.alipay.api.AlipayApiException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mmall.config.UserInfoConfig;
import com.mmall.config.alipay.AlipayTrade;
import com.mmall.dao.SystemPriceMapper;
import com.mmall.dao.UseTermMapper;
import com.mmall.model.Response.InfoEnums;
import com.mmall.model.Response.Result;
import com.mmall.model.SysUserInfo;
import com.mmall.model.SystemPrice;
import com.mmall.model.UseTerm;
import com.mmall.util.DateTimeUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.List;

@Api(value = "AlipayController", description = "支付宝支付")
@RestController
@RequestMapping("/public/alipay")
public class AlipayController {
    @Autowired
    private SystemPriceMapper systemPriceMapper;
    @Autowired
    private UseTermMapper useTermMapper;

    @ApiOperation(value = "获取系统价格",  notes="需要Authorization")
    @GetMapping(value = "/sysPrice",produces = {"application/json;charest=Utf-8"})
    public Result getSysPrice(){
        List<SystemPrice> systemPrices = systemPriceMapper.selectList(new QueryWrapper<SystemPrice>()
                .in("status", 1, 2));
        return Result.ok(systemPrices);
    }

    @ApiOperation(value = "支付",  notes="需要Authorization")
    @PostMapping(value = "/{sysId}",produces = {"application/json;charest=Utf-8"})
    public Result saveNotice(@PathVariable("sysId") String sysId) throws AlipayApiException {
        /*SystemPrice systemPrice = systemPriceMapper.selectById(sysId);
        SysUserInfo user = UserInfoConfig.getUserInfo();
        UseTerm useTerm = useTermMapper.selectOne(new QueryWrapper<UseTerm>().eq("user_id", user.getId()));*/
        long sysTime = System.currentTimeMillis();
        //当前时间是否大于使用截止时间
        /*long add = sysTime-Long.parseLong(useTerm.getClosingDate());
        String dateTime = null;
        if(add>=0){
            dateTime = DateTimeUtil.addDateNum(sysTime,systemPrice.getMonthNum())+"";
        }else {
            dateTime = DateTimeUtil.addDateNum(sysTime,systemPrice.getMonthNum())-add+"";
        }
        useTerm.setClosingDate(dateTime);
        useTermMapper.updateById(useTerm);*/
        return Result.ok(AlipayTrade.pay(sysId+"","blbl"+sysTime));
    }

    @ApiOperation(value = "notify",  notes="不需要Authorization")
    @PostMapping(value = "/notify",produces = {"application/json;charest=Utf-8"})
    public void notify(HttpServletRequest request) throws UnsupportedEncodingException {
        try {
            AlipayTrade.notifyUrl(request);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
    }


}
