package com.mmall.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mmall.model.Total;

/**
 * <p>
 * 月计表(客户账单) Mapper 接口
 * </p>
 *
 * @author qty
 * @since 2018-09-25
 */
public interface TotalMapper extends BaseMapper<Total> {
    void insertTotal(Total total);
}
