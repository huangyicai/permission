package com.mmall.controller.express;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mmall.model.City;
import com.mmall.model.Response.Result;
import com.mmall.model.SysUserInfo;
import com.mmall.service.ExpressUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  个人中心
 * </p>
 *
 * @author hyc
 * @since 2018-09-28
 */
@Api(value = "UserCenterController", description = "个人中心")
@RestController
@RequestMapping("/express/userCenter")
public class UserCenterController {

    @Autowired
    private ExpressUserService expressUserService;



}
