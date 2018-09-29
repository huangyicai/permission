package com.mmall.service;

import com.mmall.model.PricingGroup;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mmall.model.Response.Result;
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
}
