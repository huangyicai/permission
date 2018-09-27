package com.mmall.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mmall.model.SysUser;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hyc
 * @since 2018-09-15
 */
@Repository
public interface SysUserMapper extends BaseMapper<SysUser> {
    SysUser findUserByusername(@Param("username") String username);

}
