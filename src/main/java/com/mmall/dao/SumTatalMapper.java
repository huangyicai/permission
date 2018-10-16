package com.mmall.dao;

import com.mmall.model.SumTatal;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 总账单 Mapper 接口
 * </p>
 *
 * @author qty
 * @since 2018-10-16
 */
public interface SumTatalMapper extends BaseMapper<SumTatal> {
    void deleteSumTotal(@Param("sumId") String sumId);
}
