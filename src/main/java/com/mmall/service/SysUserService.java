package com.mmall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mmall.dto.SysMenuDto;
import com.mmall.dto.SysUserInfoDto;
import com.mmall.model.Response.Result;
import com.mmall.model.*;
import com.mmall.model.params.UserInfoExpressParm;
import com.mmall.model.params.UserInfoServiceParm;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hyc
 * @since 2018-09-15
 */
public interface SysUserService extends IService<SysUser> {
    /**
     * 获取菜单
     * @param user
     * @param platId
     * @return
     */
    Result<List<SysMenuDto>> findAllMenuByUser(SysUserInfo user,Integer platId);

    /**
     * 登录
     * @param username
     * @param password
     * @return
     */
    Result<AuthInfo<SysUserInfo>> login(String username, String password);

    /**
     * fn注册
     * @return
     */
    Result fnRegister(UserInfoExpressParm user,SysUserInfo parent);

    /**
     * 获取快递公司列表
     * @param user
     * @return
     */
    Result<List<SysUserInfo>> getCompanys(SysUserInfo user);

    /**
     * 冻结
     * @param id
     * @return
     */
    Result frozen(Integer id);

    /**
     * 删除
     * @param id
     * @return
     */
    Result deleteUser(Integer id);

    /**
     * 修改信息
     * @param user
     * @return
     */
    Result updateExpress(UserInfoServiceParm user);

}
