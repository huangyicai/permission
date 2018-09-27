package com.mmall.controller.express;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mmall.dto.BillDto;
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
        Total one = totalService.getOne(new QueryWrapper<Total>().eq("total_time", billParam.getDate()).eq("name", billParam.getName()));
        WeightCalculate weightCalculate = weightCalculateService.getOne(new QueryWrapper<WeightCalculate>().eq("total_id", one.getTotalId()));

        weightCalculate.setZero(weightCalculate.getZero().divide(one.getTotalWeight(),2, RoundingMode.DOWN).multiply(new BigDecimal(100)));
        weightCalculate.setOne(weightCalculate.getOne().divide(one.getTotalWeight(),2, RoundingMode.DOWN).multiply(new BigDecimal(100)));
        weightCalculate.setTwo(weightCalculate.getTwo().divide(one.getTotalWeight(),2, RoundingMode.DOWN).multiply(new BigDecimal(100)));
        weightCalculate.setThree(weightCalculate.getThree().divide(one.getTotalWeight(),2, RoundingMode.DOWN).multiply(new BigDecimal(100)));
        weightCalculate.setFour(weightCalculate.getFour().divide(one.getTotalWeight(),2, RoundingMode.DOWN).multiply(new BigDecimal(100)));
        weightCalculate.setFive(weightCalculate.getFive().divide(one.getTotalWeight(),2, RoundingMode.DOWN).multiply(new BigDecimal(100)));
        weightCalculate.setSix(weightCalculate.getSix().divide(one.getTotalWeight(),2, RoundingMode.DOWN).multiply(new BigDecimal(100)));
        weightCalculate.setSeven(weightCalculate.getSeven().divide(one.getTotalWeight(),2, RoundingMode.DOWN).multiply(new BigDecimal(100)));
        weightCalculate.setEight(weightCalculate.getEight().divide(one.getTotalWeight(),2, RoundingMode.DOWN).multiply(new BigDecimal(100)));
        weightCalculate.setNine(weightCalculate.getNine().divide(one.getTotalWeight(),2, RoundingMode.DOWN).multiply(new BigDecimal(100)));
        weightCalculate.setTen(weightCalculate.getTen().divide(one.getTotalWeight(),2, RoundingMode.DOWN).multiply(new BigDecimal(100)));
        weightCalculate.setEleven(weightCalculate.getEleven().divide(one.getTotalWeight(),2, RoundingMode.DOWN).multiply(new BigDecimal(100)));
        weightCalculate.setTwelve(weightCalculate.getTwelve().divide(one.getTotalWeight(),2, RoundingMode.DOWN).multiply(new BigDecimal(100)));
        weightCalculate.setThirteen(weightCalculate.getThirteen().divide(one.getTotalWeight(),2, RoundingMode.DOWN).multiply(new BigDecimal(100)));
        weightCalculate.setFourteen(weightCalculate.getFourteen().divide(one.getTotalWeight(),2, RoundingMode.DOWN).multiply(new BigDecimal(100)));
        weightCalculate.setFifteen(weightCalculate.getFifteen().divide(one.getTotalWeight(),2, RoundingMode.DOWN).multiply(new BigDecimal(100)));
        weightCalculate.setSixteen(weightCalculate.getSixteen().divide(one.getTotalWeight(),2, RoundingMode.DOWN).multiply(new BigDecimal(100)));
        weightCalculate.setSeventeen(weightCalculate.getSeventeen().divide(one.getTotalWeight(),2, RoundingMode.DOWN).multiply(new BigDecimal(100)));
        weightCalculate.setEighteen(weightCalculate.getEighteen().divide(one.getTotalWeight(),2, RoundingMode.DOWN).multiply(new BigDecimal(100)));
        weightCalculate.setNineteen(weightCalculate.getNineteen().divide(one.getTotalWeight(),2, RoundingMode.DOWN).multiply(new BigDecimal(100)));
        weightCalculate.setTwenty(weightCalculate.getTwenty().divide(one.getTotalWeight(),2, RoundingMode.DOWN).multiply(new BigDecimal(100)));
        weightCalculate.setTwentyOne(weightCalculate.getTwentyOne().divide(one.getTotalWeight(),2, RoundingMode.DOWN).multiply(new BigDecimal(100)));
        return Result.ok(weightCalculate);
    }
}

