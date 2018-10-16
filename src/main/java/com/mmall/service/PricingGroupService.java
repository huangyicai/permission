package com.mmall.service;

import com.mmall.model.PricingGroup;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mmall.model.Response.Result;
import com.mmall.model.SpecialPricingGroup;
import com.mmall.model.SysUserInfo;
import com.mmall.model.params.PricingGroupParam;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hyc
 * @since 2018-09-28
 */
public interface PricingGroupService extends IService<PricingGroup> {
    /**
     * 获取省份定价
     * @param userId
     * @param cityId
     * @return
     */
    Result<Map<String, List<PricingGroup>>> getPricingGroup(Integer userId, Integer cityId);

    /**
     * 添加/修改省份定价
     * @param pricingGroups
     * @return
     */
    Result savePricingGroup(List<PricingGroupParam> pricingGroups,Integer userId,Integer cityId);

    /**
     * 获取已上传的定价组
     * @param userId
     * @return
     */
    Result getAllPricingGroups(Integer userId);

    /**
     * 获取所有客户
     * @return
     */
    Result getAllCustomers(SysUserInfo userInfo);

    /**
     * 添加客户相同定价组
     * @param userId
     * @param selfId
     * @return
     */
    Result saveExistingPricingGroups(Integer userId, Integer selfId);

    /**
     * 获取用户特殊定价
     * @param specialId
     * @return
     */
    Result<Map<String, List<SpecialPricingGroup>>> getSpecialPricingGroupByKey( Integer specialId);

    /**
     * 添加特殊定价
     * @param pricingGroups
     * @param userId
     * @return
     */
    Result saveSpecialPricingGroup(List<PricingGroupParam> pricingGroups, Integer userId,Integer keyId);

    /**
     * 修改特殊定价
     * @param pricingGroups
     * @param keyId
     * @return
     */
    Result updateSpecialPricingGroup(List<PricingGroupParam> pricingGroups, Integer keyId);

    /**
     * 删除特殊定价
     * @param keyId
     * @return
     */
    Result deleteSpecialPricingGroup(Integer keyId);
}
