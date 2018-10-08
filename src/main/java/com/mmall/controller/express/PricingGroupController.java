package com.mmall.controller.express;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.mmall.config.UserInfoConfig;
import com.mmall.dao.CityMapper;
import com.mmall.model.City;
import com.mmall.model.PricingGroup;
import com.mmall.model.Response.Result;
import com.mmall.model.SysUserInfo;
import com.mmall.model.params.PricingGroupParam;
import com.mmall.service.PricingGroupService;
import com.mmall.util.BeanValidator;
import com.mmall.vo.CityVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;

import java.lang.reflect.InvocationTargetException;
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
    @GetMapping(value = "/cityList/{userId}",produces = {"application/json;charest=Utf-8"})
    public Result<List<CityVo>> getCusmotersInfo(@PathVariable("userId") Integer userId) throws InvocationTargetException, IllegalAccessException {
        Result allPricingGroups = pricingGroupService.getAllPricingGroups(userId);
        List<City> cities = cityMapper.selectList(new QueryWrapper<City>());
        List<City> data = (List<City>)allPricingGroups.getData();

        List<CityVo> cityVos = Lists.newArrayList();
        for(City city : cities){
            CityVo adapt = new CityVo();
            adapt.setId(city.getId());
            adapt.setProvinceKey(city.getProvinceKey());
            adapt.setProvinceName(city.getProvinceName());
            for(City city1:data){
                if(city1.equals(city)){
                    adapt.setStatus(true);
                    continue;
                }
            }
            cityVos.add(adapt);
        }
        return Result.ok(cityVos);
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

    @ApiOperation(value = "获取所有客户",  notes="需要Authorization")
    @GetMapping(value = "/customers",produces = {"application/json;charest=Utf-8"})
    public Result getAllCustomers(){
        SysUserInfo userInfo = UserInfoConfig.getUserInfo();
        return pricingGroupService.getAllCustomers(userInfo);
    }

    @ApiOperation(value = "添加客户相同定价组",  notes="需要Authorization")
    @PostMapping(value = "/customer/{selfId}/{userId}",produces = {"application/json;charest=Utf-8"})
    public Result saveExistingPricingGroups(@PathVariable("userId")Integer userId,
                                            @PathVariable("selfId")Integer selfId){
        return pricingGroupService.saveExistingPricingGroups(userId,selfId);
    }


}

