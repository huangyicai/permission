package com.mmall.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mmall.model.ProvinceCalculate;
import com.mmall.model.WeightCalculate;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 重量区间计算表 Mapper 接口
 * </p>
 *
 * @author qty
 * @since 2018-09-25
 */
public interface WeightCalculateMapper extends BaseMapper<WeightCalculate> {
    WeightCalculate getWeightCalculate(@Param("totalId") String totalId);

    void updateByTotalId(WeightCalculate weightCalculate);

    void deleteByTotalId(@Param("idStr") String idStr);
}
