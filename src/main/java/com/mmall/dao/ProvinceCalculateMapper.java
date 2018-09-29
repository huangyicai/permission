package com.mmall.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mmall.model.ProvinceCalculate;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 省计表 Mapper 接口
 * </p>
 *
 * @author qty
 * @since 2018-09-25
 */
public interface ProvinceCalculateMapper extends BaseMapper<ProvinceCalculate> {
    ProvinceCalculate getProvinceCalculate(@Param("totalId") String totalId);
}
