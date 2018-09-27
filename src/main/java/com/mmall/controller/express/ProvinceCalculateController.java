package com.mmall.controller.express;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mmall.dto.BillDto;
import com.mmall.model.ProvinceCalculate;
import com.mmall.model.Response.Result;
import com.mmall.model.Total;
import com.mmall.model.params.BillParam;
import com.mmall.service.ProvinceCalculateService;
import com.mmall.service.TotalService;
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
 * 省计表 前端控制器
 * </p>
 *
 * @author qty
 * @since 2018-09-25
 */
@Api(value = "ProvinceCalculateController", description = "省计表管理")
@RestController
@RequestMapping("/express/provinceCalculate")
public class ProvinceCalculateController {

    @Autowired
    private ProvinceCalculateService provinceCalculateService;

    @Autowired
    private TotalService totalService;

    @ApiOperation(value = "获取客户省计数据",  notes="需要Authorization")
    @PostMapping(value = "/getProvinceCalculate")
    public Result<ProvinceCalculate> getProvinceCalculate(BillParam billParam){
        Total one = totalService.getOne(new QueryWrapper<Total>().eq("total_time", billParam.getDate()).eq("user_id", billParam.getId()));
        ProvinceCalculate totalId =null;
        if(one!=null){
            totalId=provinceCalculateService.getOne(new QueryWrapper<ProvinceCalculate>().eq("total_id", one.getTotalId()));

        }
        return Result.ok(totalId);
    }

}

