package com.mmall.controller.express;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mmall.dao.CityMapper;
import com.mmall.model.City;
import com.mmall.model.PricingGroup;
import com.mmall.model.Response.Result;
import com.mmall.model.SysUserInfo;
import com.mmall.model.params.PricingGroupParam;
import com.mmall.service.PricingGroupService;
import com.mmall.util.BeanValidator;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  定价组管理
 * </p>
 *
 * @author hyc
 * @since 2018-09-28
 */
@Api(value = "PricingGroupController", description = "定价组管理")
@RestController
@RequestMapping("/express/pricingGroup")
public class PricingGroupController {
    @Autowired
    private PricingGroupService pricingGroupService;
    @Autowired
    private CityMapper cityMapper;
    @ApiOperation(value = "获取省份",  notes="需要Authorization")
    @GetMapping(value = "/cityList",produces = {"application/json;charest=Utf-8"})
    public Result<List<City>> getCusmotersInfo(){
        //Result allPricingGroups = pricingGroupService.getAllPricingGroups(userId);
        List<City> cities = cityMapper.selectList(new QueryWrapper<City>());
       /* List<City> data = (List<City>)allPricingGroups.getData();
        for(City city :cities){
            for(City city1:data){
                if(city.getId().equals(city1.getId())||city.getId()==city1.getId()){

                }
            }
        }*/
        return Result.ok(cities);
    }

    @ApiOperation(value = "获取省份定价",  notes="需要Authorization")
    @GetMapping(value = "/{userId}/{cityId}",produces = {"application/json;charest=Utf-8"})
    public Result<Map<String,List<PricingGroup>>> getPricingGroup(@PathVariable("userId")Integer userId,
                                                                  @PathVariable() Integer cityId){
        return pricingGroupService.getPricingGroup(userId,cityId);
    }
    @ApiOperation(value = "添加/修改省份定价",  notes="需要Authorization")
    @PostMapping(value = "/{userId}/{cityId}",produces = {"application/json;charest=Utf-8"})
    public Result savePricingGroup(@RequestBody List<PricingGroupParam> pricingGroups,
                                   @PathVariable("userId")Integer userId,
                                   @PathVariable() Integer cityId){
        for(PricingGroupParam pgp :pricingGroups){
            BeanValidator.check(pgp);
        }
        return pricingGroupService.savePricingGroup(pricingGroups,userId,cityId);
    }

    @ApiOperation(value = "获取已上传的省份定价组",  notes="需要Authorization")
    @GetMapping(value = "/upload/{userId}",produces = {"application/json;charest=Utf-8"})
    public Result getAllPricingGroups(@PathVariable("userId")Integer userId){
        return pricingGroupService.getAllPricingGroups(userId);
    }


}

