package com.mmall.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.mmall.constants.LevelConstants;
import com.mmall.dao.*;
import com.mmall.dto.SysMenuDto;
import com.mmall.model.*;
import com.mmall.model.Response.InfoEnums;
import com.mmall.model.Response.Result;
import com.mmall.model.params.PricingGroupParam;
import com.mmall.service.PricingGroupService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mmall.util.LevelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hyc
 * @since 2018-09-28
 */
@Service
public class PricingGroupServiceImpl extends ServiceImpl<PricingGroupMapper, PricingGroup> implements PricingGroupService {
    @Resource
    private PricingGroupMapper pricingGroupMapper;
    @Autowired
    private CityMapper cityMapper;
    @Autowired
    private SysUserInfoMapper sysUserInfoMapper;
    @Autowired
    private SpecialPricingGroupMapper specialPricingGroupMapper;
    @Autowired
    private SpecialPricingGroupKeyMapper specialPricingGroupKeyMapper;

    public Result<Map<String, List<PricingGroup>>> getPricingGroup(Integer userId, Integer cityId) {
        List<PricingGroup> pricingGroups = pricingGroupMapper.selectList(new QueryWrapper<PricingGroup>()
                .eq("user_id", userId)
                .eq("city_id", cityId));
        Multimap<String, PricingGroup> weightMap = ArrayListMultimap.create();
        String firstWeight = "firstWeight";//首重
        String continuedWeight = "continuedWeight";//续重
        for(PricingGroup pg:pricingGroups){
            weightMap.put(pg.getFirstOrContinued()==1?firstWeight:continuedWeight,pg);
        }
        Map<String,List<PricingGroup>> map = Maps.newHashMap();
        //首重集合
        List<PricingGroup> firstWeightList = (List<PricingGroup>) weightMap.get(firstWeight);
        //续重集合
        List<PricingGroup> continuedWeightList = (List<PricingGroup>) weightMap.get(continuedWeight);

        map.put(firstWeight,firstWeightList);
        map.put(continuedWeight,continuedWeightList);
        return Result.ok(map);
    }
    @Transactional
    public Result savePricingGroup(List<PricingGroupParam> pricingGroups,Integer userId,Integer cityId) {
        boolean firstWeight = true;
        boolean continuedWeight = true;
        for(PricingGroupParam pg:pricingGroups){
            if(pg.getFirstOrContinued()==1)firstWeight=false;
            if(pg.getFirstOrContinued()==2)continuedWeight=false;
        }
        if(firstWeight||continuedWeight) return Result.error(InfoEnums.WEIGHT_NOT_WRITE);


        pricingGroupMapper.delete(new QueryWrapper<PricingGroup>().eq("user_id",userId).eq("city_id",cityId));
        List<PricingGroup> pricingGroupList = Lists.newArrayList();
        for(PricingGroupParam prp : pricingGroups){
            PricingGroup pg = PricingGroup.builder()
                    .userId(userId)
                    .cityId(cityId)
                    .areaBegin(prp.getAreaBegin())
                    .areaEnd(prp.getAreaEnd())
                    .weightStandard(prp.getWeightStandard())
                    .price(prp.getPrice())
                    .firstOrContinued(prp.getFirstOrContinued())
                    .firstWeightPrice(prp.getFirstWeightPrice())
                    .firstWeight(prp.getFirstWeight())
                    .build();
            pricingGroupList.add(pg);
            pricingGroupMapper.insert(pg);
        }
        //pricingGroupMapper.insertPricingGroupList(pricingGroupList);
        return Result.ok();
    }

    public Result getAllPricingGroups(Integer userId) {
        List<Integer> allCityId = pricingGroupMapper.getAllPricingGroups(userId);
        if(allCityId.isEmpty()){
            return Result.ok(allCityId);
        }
        String ids = "";
        for(Integer i : allCityId){
            ids += ","+i;
        }
        List<City> cityIds = cityMapper.getAllById(ids);
        return Result.ok(cityIds);
    }

    @Override
    public Result getAllCustomers(SysUserInfo userInfo) {
        String level = LevelUtil.calculateLevel(userInfo.getLevel(), userInfo.getId());
        List<SysUserInfo> allLikeLevel = sysUserInfoMapper.findAllLikeLevel(level+"%");
        return Result.ok(allLikeLevel);
    }

    @Override
    @Transactional
    public Result saveExistingPricingGroups(Integer userId, Integer selfId) {
        pricingGroupMapper.delete(new QueryWrapper<PricingGroup>().eq("user_id",selfId));

        List<PricingGroup> pricingGroupList = pricingGroupMapper.selectList(new QueryWrapper<PricingGroup>().eq("user_id", userId));
        for(PricingGroup pg:pricingGroupList){
            pg.setUserId(selfId);
            pricingGroupMapper.insert(pg);
        }
        return Result.ok();
    }

    @Override
    public Result<Map<String, List<SpecialPricingGroup>>> getSpecialPricingGroupByKey( Integer specialId) {
        List<SpecialPricingGroup> pricingGroups = specialPricingGroupMapper.selectList(new QueryWrapper<SpecialPricingGroup>()
                .eq("key_id", specialId));
        Multimap<String, SpecialPricingGroup> weightMap = ArrayListMultimap.create();
        String firstWeight = "firstWeight";//首重
        String continuedWeight = "continuedWeight";//续重
        for(SpecialPricingGroup pg:pricingGroups){
            weightMap.put(pg.getFirstOrContinued()==1?firstWeight:continuedWeight,pg);
        }
        Map<String,List<SpecialPricingGroup>> map = Maps.newHashMap();
        //首重集合
        List<SpecialPricingGroup> firstWeightList = (List<SpecialPricingGroup>) weightMap.get(firstWeight);
        //续重集合
        List<SpecialPricingGroup> continuedWeightList = (List<SpecialPricingGroup>) weightMap.get(continuedWeight);

        map.put(firstWeight,firstWeightList);
        map.put(continuedWeight,continuedWeightList);
        return Result.ok(map);
    }

    @Override
    public Result saveSpecialPricingGroup(List<PricingGroupParam> pricingGroups, Integer userId) {
        if(pricingGroups.isEmpty()){
            return Result.error(InfoEnums.PLEASE_ADD_PRICING);
        }
        boolean firstWeight = true;
        boolean continuedWeight = true;
        for(PricingGroupParam pg:pricingGroups){
            if(pg.getFirstOrContinued()==1)firstWeight=false;
            if(pg.getFirstOrContinued()==2)continuedWeight=false;
        }

        String keyName = pricingGroups.get(0).getKeyName();
        if(firstWeight||continuedWeight) return Result.error(InfoEnums.WEIGHT_NOT_WRITE);
        SpecialPricingGroupKey key_name = specialPricingGroupKeyMapper.selectOne(new QueryWrapper<SpecialPricingGroupKey>().eq("key_name", keyName));
        if(key_name!=null){
            return Result.error(InfoEnums.KEY_EXISTENCE);
        }
        SpecialPricingGroupKey specialPricingGroupKey = new SpecialPricingGroupKey();
        specialPricingGroupKey.setKeyName(keyName);
        specialPricingGroupKey.setUserId(userId);
        specialPricingGroupKeyMapper.insert(specialPricingGroupKey);
        //specialPricingGroupMapper.delete(new QueryWrapper<PricingGroup>().eq("user_id",userId).eq("city_id",cityId));
        List<SpecialPricingGroup> pricingGroupList = Lists.newArrayList();
        for(PricingGroupParam prp : pricingGroups){
            SpecialPricingGroup pg = SpecialPricingGroup.builder()
                    .keyId(specialPricingGroupKey.getId())
                    .areaBegin(prp.getAreaBegin())
                    .areaEnd(prp.getAreaEnd())
                    .weightStandard(prp.getWeightStandard())
                    .price(prp.getPrice())
                    .firstOrContinued(prp.getFirstOrContinued())
                    .firstWeightPrice(prp.getFirstWeightPrice())
                    .firstWeight(prp.getFirstWeight())
                    .build();
            pricingGroupList.add(pg);
            specialPricingGroupMapper.insert(pg);
        }
        //pricingGroupMapper.insertPricingGroupList(pricingGroupList);
        return Result.ok();
    }

    @Override
    @Transactional
    public Result updateSpecialPricingGroup(List<PricingGroupParam> pricingGroups, Integer keyId) {
        boolean firstWeight = true;
        boolean continuedWeight = true;
        for(PricingGroupParam pg:pricingGroups){
            if(pg.getFirstOrContinued()==1)firstWeight=false;
            if(pg.getFirstOrContinued()==2)continuedWeight=false;
        }
        if(firstWeight||continuedWeight) return Result.error(InfoEnums.WEIGHT_NOT_WRITE);
        specialPricingGroupMapper.delete(new QueryWrapper<SpecialPricingGroup>().eq("key_id",keyId));
        List<SpecialPricingGroup> pricingGroupList = Lists.newArrayList();
        for(PricingGroupParam prp : pricingGroups){
            SpecialPricingGroup pg = SpecialPricingGroup.builder()
                    .keyId(keyId)
                    .areaBegin(prp.getAreaBegin())
                    .areaEnd(prp.getAreaEnd())
                    .weightStandard(prp.getWeightStandard())
                    .price(prp.getPrice())
                    .firstOrContinued(prp.getFirstOrContinued())
                    .firstWeightPrice(prp.getFirstWeightPrice())
                    .firstWeight(prp.getFirstWeight())
                    .build();
            pricingGroupList.add(pg);
            specialPricingGroupMapper.insert(pg);
        }
        //pricingGroupMapper.insertPricingGroupList(pricingGroupList);
        return Result.ok();
    }

    @Override
    @Transactional
    public Result deleteSpecialPricingGroup(Integer keyId) {
        specialPricingGroupKeyMapper.deleteById(keyId);
        specialPricingGroupMapper.delete(new QueryWrapper<SpecialPricingGroup>().eq("key_id",keyId));
        return Result.ok();
    }
}
