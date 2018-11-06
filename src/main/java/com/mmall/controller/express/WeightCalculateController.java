package com.mmall.controller.express;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mmall.config.UserInfoConfig;
import com.mmall.dto.BillDto;
import com.mmall.dto.WeightCalculateDto;
import com.mmall.model.Response.InfoEnums;
import com.mmall.model.Response.Result;
import com.mmall.model.SysUserInfo;
import com.mmall.model.Total;
import com.mmall.model.WeightCalculate;
import com.mmall.model.params.BillParam;
import com.mmall.service.SysUserInfoService;
import com.mmall.service.TotalService;
import com.mmall.service.WeightCalculateService;
import com.mmall.util.DateUtils;
import com.mmall.util.LevelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 重量区间计算表 前端控制器
 * </p>
 *
 * @author qty
 * @since 2018-09-25
 */
@Api(value = "WeightCalculateController", description = "重量区间计算表管理")
@RestController
@RequestMapping("/express/weightCalculate")
public class WeightCalculateController {

    @Autowired
    private WeightCalculateService weightCalculateService;

    @ApiOperation(value = "获取重量区间占比",  notes="需要Authorization")
    @PostMapping(value = "/getWeightCalculate")
    public Result<List<WeightCalculateDto>> getWeightCalculate(@RequestBody BillParam billParam){
        List<WeightCalculateDto> weightCalculate = weightCalculateService.getWeightCalculate(billParam);
        return Result.ok(weightCalculate);
    }
}

