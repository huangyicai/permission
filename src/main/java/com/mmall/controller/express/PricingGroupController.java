package com.mmall.controller.express;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.mmall.config.UserInfoConfig;
import com.mmall.dao.CityMapper;
import com.mmall.dao.SpecialPricingGroupKeyMapper;
import com.mmall.model.*;
import com.mmall.model.Response.InfoEnums;
import com.mmall.model.Response.Result;
import com.mmall.model.params.PricingGroupParam;
import com.mmall.model.params.keyNameParam;
import com.mmall.service.PrepaidService;
import com.mmall.service.PricingGroupService;
import com.mmall.util.BeanValidator;
import com.mmall.vo.CityVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

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

    @Autowired
    private SpecialPricingGroupKeyMapper specialPricingGroupKeyMapper;

    @Autowired
    private PrepaidService prepaidService;

    @ApiOperation(value = "获取省份",  notes="需要Authorization")
    @GetMapping(value = "/cityList/{userId}",produces = {"application/json;charest=Utf-8"})
    public Result<List<CityVo>> getCusmotersInfo(@PathVariable("userId") Integer userId) throws InvocationTargetException, IllegalAccessException {
        if(userId==0||userId.equals(0)){
            SysUserInfo userInfo = UserInfoConfig.getUserInfo();
            userId = userInfo.getId();
        }
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
        if(userId==0||userId.equals(0)){
            SysUserInfo userInfo = UserInfoConfig.getUserInfo();
            userId = userInfo.getId();
        }
        return pricingGroupService.getPricingGroup(userId,cityId);
    }


    @ApiOperation(value = "删除特殊定价",  notes="需要Authorization")
    @DeleteMapping(value = "/{keyId}",produces = {"application/json;charest=Utf-8"})
    public Result deleteSpecialPricingGroup(@PathVariable("keyId")Integer keyId){

        return pricingGroupService.deleteSpecialPricingGroup(keyId);
    }

    @ApiOperation(value = "添加特殊定价",  notes="需要Authorization")
    @PostMapping(value = "/special/{keyId}/{userId}",produces = {"application/json;charest=Utf-8"})
    public Result saveSpecialPricingGroup(@RequestBody List<PricingGroupParam> pricingGroups,
                                          @PathVariable("keyId")Integer keyId,
                                          @PathVariable("userId")Integer userId){
        for(PricingGroupParam pgp :pricingGroups){
            BeanValidator.check(pgp);
        }
        if(userId==0||userId.equals(0)){
            SysUserInfo userInfo = UserInfoConfig.getUserInfo();
            userId = userInfo.getId();
        }
        return pricingGroupService.saveSpecialPricingGroup(pricingGroups,userId,keyId);
    }

    @ApiOperation(value = "修改特殊定价",  notes="需要Authorization")
    @PutMapping(value = "/{keyId}",produces = {"application/json;charest=Utf-8"})
    public Result updateSpecialPricingGroup(@RequestBody List<PricingGroupParam> pricingGroups,
                                          @PathVariable("keyId")Integer keyId){
        for(PricingGroupParam pgp :pricingGroups){
            BeanValidator.check(pgp);
        }
        return pricingGroupService.updateSpecialPricingGroup(pricingGroups,keyId);
    }

    @ApiOperation(value = "添加特殊定价关键字",  notes="需要Authorization")
    @PostMapping(value = "/special/key/{userId}/{rpId}",produces = {"application/json;charest=Utf-8"})
    public Result saveSpecialPricingGroupKey(@PathVariable("userId")Integer userId,
                                             @PathVariable("rpId")Integer rpId,
                                             @RequestBody keyNameParam keyName){
        if(userId==0||userId.equals(0)){
            SysUserInfo userInfo = UserInfoConfig.getUserInfo();
            userId = userInfo.getId();
        }
        SpecialPricingGroupKey key_name = specialPricingGroupKeyMapper.selectOne(new QueryWrapper<SpecialPricingGroupKey>()
                .eq("key_name", keyName.getKeyName())
                .eq("user_id",userId));

        if(key_name!=null){
            return Result.error(InfoEnums.KEY_EXISTENCE);
        }
        SpecialPricingGroupKey specialPricingGroupKey = new SpecialPricingGroupKey();
        specialPricingGroupKey.setKeyName(keyName.getKeyName());
        specialPricingGroupKey.setUserId(userId);
        specialPricingGroupKey.setStatus(rpId);
        specialPricingGroupKeyMapper.insert(specialPricingGroupKey);
        return Result.ok(specialPricingGroupKey);
    }

    @ApiOperation(value = "获取用户特殊定价关键字",  notes="需要Authorization")
    @GetMapping(value = "/specialKey/{userId}",produces = {"application/json;charest=Utf-8"})
    public Result<List<SpecialPricingGroupKey>> getSpecialPricingGroup(@PathVariable("userId")Integer userId){
        if(userId==0||userId.equals(0)){
            SysUserInfo userInfo = UserInfoConfig.getUserInfo();
            userId = userInfo.getId();
        }
        return Result.ok(specialPricingGroupKeyMapper.selectList(new QueryWrapper<SpecialPricingGroupKey>().eq("user_id",userId)));
    }

    @ApiOperation(value = "获取用户特殊定价（通过关键字ID）",  notes="需要Authorization")
    @GetMapping(value = "/special/{specialId}",produces = {"application/json;charest=Utf-8"})
    public Result<Map<String,List<SpecialPricingGroup>>> getSpecialPricingGroupByKey(@PathVariable("specialId")Integer specialId){

        return pricingGroupService.getSpecialPricingGroupByKey(specialId);
    }

    @ApiOperation(value = "添加/修改省份定价",  notes="需要Authorization")
    @PostMapping(value = "/{userId}/{cityId}",produces = {"application/json;charest=Utf-8"})
    public Result savePricingGroup(@RequestBody List<PricingGroupParam> pricingGroups,
                                   @PathVariable("userId")Integer userId,
                                   @PathVariable() Integer cityId){
        for(PricingGroupParam pgp :pricingGroups){
            BeanValidator.check(pgp);
        }
        if(userId==0||userId.equals(0)){
            SysUserInfo userInfo = UserInfoConfig.getUserInfo();
            userId = userInfo.getId();
        }
        return pricingGroupService.savePricingGroup(pricingGroups,userId,cityId);
    }

    @ApiOperation(value = "获取已上传的省份定价组",  notes="需要Authorization")
    @GetMapping(value = "/upload/{userId}",produces = {"application/json;charest=Utf-8"})
    public Result getAllPricingGroups(@PathVariable("userId")Integer userId){
        if(userId==0||userId.equals(0)){
            SysUserInfo userInfo = UserInfoConfig.getUserInfo();
            userId = userInfo.getId();
        }
        return pricingGroupService.getAllPricingGroups(userId);
    }

    @ApiOperation(value = "获取所有客户",  notes="需要Authorization")
    @GetMapping(value = "/customers",produces = {"application/json;charest=Utf-8"})
    public Result getAllCustomers(){
        SysUserInfo userInfo = UserInfoConfig.getUserInfo();
        return pricingGroupService.getAllCustomers(userInfo);
    }

    @ApiOperation(value = "批量添加定价组",  notes="需要Authorization")
    @PostMapping(value = "/customer/{pgsId}/{cityId}/{userId}",produces = {"application/json;charest=Utf-8"})
    public Result saveExistingPricingGroups(@PathVariable("pgsId")String pgsId,
                                            @PathVariable("cityId")Integer cityId,
                                            @PathVariable("userId")Integer userId){
        return pricingGroupService.saveAllExistingPricingGroups(pgsId,cityId,userId);
    }

    @ApiOperation(value = "添加客户相同定价组",  notes="需要Authorization")
    @PostMapping(value = "/customer/type/{selfId}/{userId}/{type}/{typeCope}",produces = {"application/json;charest=Utf-8"})
    public Result saveExistingPricingGroups(@PathVariable("userId")Integer userId,
                                            @PathVariable("selfId")Integer selfId,
                                            @PathVariable("type")Integer type,
                                            @PathVariable("typeCope")Integer typeCope){
        return pricingGroupService.saveExistingPricingGroups(userId,selfId,type,typeCope);
    }

    @ApiOperation(value = "导入省份定价",  notes="需要Authorization")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId",value = "用户id",dataType = "Integer",paramType = "path")
    })
    @PostMapping(value = "/importPrice/{userId}",produces = {"application/json;charest=Utf-8"})
    public Result importPrice(@RequestParam(value = "file") MultipartFile file,
                              @PathVariable("userId")Integer userId) throws IOException, ExecutionException, InterruptedException {
        return pricingGroupService.importPrice(file,userId);
    }

    @ApiOperation(value = "输入预付金额/每单",  notes="需要Authorization")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "money",value = "预付金额/每单",dataType = "String",paramType = "path"),
            @ApiImplicitParam(name = "userId",value = "快递公司对应客户的用户id",dataType = "long",paramType = "path")
    })
    @PostMapping(value = "/savePrepaid/{money}/{userId}",produces = {"application/json;charest=Utf-8"})
    public Result savePrepaid(@PathVariable("money") String money,@PathVariable("userId")Integer userId){
        Prepaid one = prepaidService.getOne(new QueryWrapper<Prepaid>().eq("user_id", userId));
        if(one==null){
            Prepaid p=new Prepaid();
            p.setMoney(new BigDecimal(money));
            p.setUserId(userId);
            prepaidService.save(p);
        }
        else{
            one.setMoney(new BigDecimal(money));
            prepaidService.updateById(one);
        }
        return Result.ok();
    }

    @ApiOperation(value = "获取客户预付金额/每单",  notes="需要Authorization")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId",value = "快递公司对应客户的用户id",dataType = "long",paramType = "path")
    })
    @GetMapping(value = "/getPrepaid/{userId}",produces = {"application/json;charest=Utf-8"})
    public Result getPrepaid(@PathVariable("userId")Integer userId){
        Prepaid one = prepaidService.getOne(new QueryWrapper<Prepaid>().eq("user_id", userId));
        return Result.ok(one);
    }

    @ApiOperation(value = "修改预付金额/每单",  notes="需要Authorization")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "money",value = "预付金额/每单",dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "prepaidId",value = "主键",dataType = "long",paramType = "query")
    })
    @PutMapping(value = "/updatePrepaid",produces = {"application/json;charest=Utf-8"})
    public Result updatePrepaid(@RequestParam(value = "money") String money,@RequestParam(value = "prepaidId")Integer prepaidId){
        Prepaid p=new Prepaid();
        p.setId(prepaidId);
        p.setMoney(new BigDecimal(money));
        prepaidService.updateById(p);
        return Result.ok();
    }
}

