package com.mmall.config;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mmall.component.ApplicationContextHelper;
import com.mmall.constants.LevelConstants;
import com.mmall.dao.SysUserInfoMapper;
import com.mmall.model.SysUserInfo;
import org.apache.shiro.SecurityUtils;

public class UserInfoConfig {
    /**
     * 获取session用户
     * @return
     */
    public static SysUserInfo getUserInfo(){
        SysUserInfo user = (SysUserInfo) SecurityUtils.getSubject().getSession().getAttribute("user");

        if(user.getPlatformId()== LevelConstants.EXPRESS||user.getPlatformId().equals(LevelConstants.EXPRESS)){
            if(user.getParentId()!=1){
                SysUserInfoMapper sysUserInfoMapper = ApplicationContextHelper.getBeanClass(SysUserInfoMapper.class);
                user = sysUserInfoMapper.selectOne(new QueryWrapper<SysUserInfo>().eq("id", user.getParentId()));
            }
        }
        return user;
    }
}
