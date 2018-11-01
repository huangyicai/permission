package com.mmall.dao;

import com.mmall.model.DailyTotal;
import com.mmall.model.ProvincialMeter;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 省计表（新） Mapper 接口
 * </p>
 *
 * @author qty
 * @since 2018-10-29
 */
public interface ProvincialMeterMapper extends BaseMapper<ProvincialMeter> {

    void deleteByTotalId(@Param("idStr") String idStr);

    List<ProvincialMeter> getDailyTotalByTotalId(@Param("userIdStr") String userIdStr);
}
