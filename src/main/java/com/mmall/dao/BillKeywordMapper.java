package com.mmall.dao;

import com.mmall.model.BillKeyword;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hyc
 * @since 2018-09-26
 */
public interface BillKeywordMapper extends BaseMapper<BillKeyword> {

    Integer insertBillKeyword(BillKeyword billKeyword);

    List<BillKeyword> getBillKeyword(@Param("nameStr") String nameStr);
}
