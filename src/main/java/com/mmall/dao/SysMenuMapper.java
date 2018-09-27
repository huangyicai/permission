package com.mmall.dao;

import com.mmall.model.SysMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hyc
 * @since 2018-09-20
 */
public interface SysMenuMapper extends BaseMapper<SysMenu> {
    List<SysMenu> findAllMenuByUser(Integer id);
}
