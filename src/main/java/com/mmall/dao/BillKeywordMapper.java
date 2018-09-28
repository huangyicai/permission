package com.mmall.dao;

import com.mmall.model.BillKeyword;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

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

}
