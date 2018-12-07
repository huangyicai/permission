package com.mmall.dao;

import com.mmall.model.SpecialPricingGroup;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mmall.vo.PricingGroupVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hyc
 * @since 2018-10-14
 */
public interface SpecialPricingGroupMapper extends BaseMapper<SpecialPricingGroup> {

    List<PricingGroupVo> getPricingGroupVo(@Param("userId") Integer UserId);

    List<SpecialPricingGroup> getSpecialPricingGroup(@Param("keyId") Integer keyId);
}
