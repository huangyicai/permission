package com.mmall.dao;

import com.mmall.model.DailyTotal;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

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
    List<DailyTotal> getDailyTotalListByTotalId(@Param("userIdStr") String userIdStr,@Param("time") String time);

    void deleteByTotalId(@Param("idStr") String idStr);
}
