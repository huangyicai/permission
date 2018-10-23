package com.mmall.controller.express;


import com.mmall.dto.DailyTotalDto;
import com.mmall.model.DailyTotal;
import com.mmall.model.Response.Result;
import com.mmall.model.params.BillParam;
import com.mmall.service.DailyTotalService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 每日单量 前端控制器
 * </p>
 *
 * @author qty
 * @since 2018-10-19
 */
@Api(value = "DailyTotalController", description = "每日单量管理")
@RestController
@RequestMapping("/express/dailyTotal")
public class DailyTotalController {

    @Autowired
    private DailyTotalService dailyTotalService;

    @ApiOperation(value = "获取相应月份的每日单量",notes = "需要Authorization")
    @PostMapping(value = "/getDailyTotalList",produces = {"application/json;charest=Utf-8"})
    public Result getDailyTotalList(@RequestBody BillParam billParam){
        List<DailyTotalDto> dailyTotalList = dailyTotalService.getDailyTotalList(billParam);
        return Result.ok(dailyTotalList);
    }
}

