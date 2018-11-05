package com.mmall.dao;

import com.mmall.model.PricingGroup;
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
 * @since 2018-09-28
 */
public interface PricingGroupMapper extends BaseMapper<PricingGroup> {
    void insertPricingGroupList(List<PricingGroup> pricingGroups);

    List<Integer> getAllPricingGroups(Integer userId);

    List<PricingGroupVo> ListPricingGroup(Integer userId);


    List<PricingGroup> getAllByUserAndCitys(@Param("pgsId")String pgsId,
                                            @Param("pgId")Integer pgId,
                                            @Param("userId")Integer userId);
}
