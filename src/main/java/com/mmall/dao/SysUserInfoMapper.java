package com.mmall.dao;

import com.mmall.model.SysUserInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hyc
 * @since 2018-09-20
 */
public interface SysUserInfoMapper extends BaseMapper<SysUserInfo> {

    String findUserName(@Param("id")Integer id);

    SysUserInfo findUserByusername(String username);

    SysUserInfo findUserInfoByid(Integer id);

    List<SysUserInfo> findAllLikeLevel(String level);

    int updateUserList(@Param("s") String s);

    int updataUserLevelAndParentId(@Param("userIds") String userIds,
                                   @Param("level")String level,
                                   @Param("toBranchId")Integer toBranchId);
}
