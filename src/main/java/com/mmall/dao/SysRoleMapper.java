package com.mmall.dao;

import com.mmall.model.SysRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import java.util.Set;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hyc
 * @since 2018-09-20
 */
public interface SysRoleMapper extends BaseMapper<SysRole> {
    Set<SysRole> findUserRoleByusername(String username);

}
