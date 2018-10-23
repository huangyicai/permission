package com.mmall.dao;

import com.mmall.model.DailyTotal;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 每日单量 Mapper 接口
 * </p>
 *
 * @author qty
 * @since 2018-10-19
 */
public interface DailyTotalMapper extends BaseMapper<DailyTotal> {
    List<DailyTotal> getDailyTotalListByTotalId(String userIdStr,String time);
}
