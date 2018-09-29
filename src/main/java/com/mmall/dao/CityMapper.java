package com.mmall.dao;

import com.mmall.model.City;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hyc
 * @since 2018-09-28
 */
public interface CityMapper extends BaseMapper<City> {

    List<City> getAllById(String ids);
}
