package com.mmall.controller.express;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mmall.dto.BillDto;
import com.mmall.model.Response.InfoEnums;
import com.mmall.model.Response.Result;
import com.mmall.model.Total;
import com.mmall.model.WeightCalculate;
import com.mmall.model.params.BillParam;
import com.mmall.service.TotalService;
import com.mmall.service.WeightCalculateService;
import com.mmall.util.DateUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

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

    @Autowired
    private TotalService totalService;

    @ApiOperation(value = "获取重量区间占比",  notes="需要Authorization")
    @PostMapping(value = "/getWeightCalculate")
    public Result<WeightCalculate> getWeightCalculate(BillParam billParam){
        List<Total> list = totalService.list(new QueryWrapper<Total>().eq("total_time", billParam.getDate()).eq("user_id", billParam.getId()));
        if(list.size()<=0){
            return Result.error(InfoEnums.DATA_IS_NULL);
        }

        String totalIdStr="";

        for(Total total:list){
            totalIdStr+=total.getTotalId()+",";
        }

        totalIdStr=totalIdStr.substring(0,totalIdStr.length()-1);
        WeightCalculate weightCalculate = weightCalculateService.getWeightCalculate(totalIdStr);
        return Result.ok(weightCalculate);
    }
}

