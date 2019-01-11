package com.mmall.service;

import com.aliyuncs.exceptions.ClientException;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mmall.dto.SysMenuDto;
import com.mmall.dto.SysUserInfoDto;
import com.mmall.model.Response.Result;
import com.mmall.model.*;
import com.mmall.model.params.UserInfoExpressParm;
import com.mmall.model.params.UserInfoServiceParm;
import com.mmall.model.params.UserPasswordParam;

import java.util.List;
import java.util.Map;

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
    Result fnRegister(UserInfoExpressParm user,SysUserInfo parent,Integer fnId);

    /**
     * 获取快递公司列表
     * @param user
     * @return
     */
    Result getCompanys(SysUserInfo user,Page ipage);

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

    /**
     * 获取自己的个人信息
     * @param user
     * @return
     */
    Result<Map<String,Object>> getUserInfo(SysUserInfo user);

    /**
     *获取验证码
     * @param phone
     * @return
     */
    Result getCode(String phone) throws ClientException;

    /**
     * 验证码修改密码
     * @param user
     * @param code
     * @return
     */
    Result updateUserPassword(SysUserInfo user, String code, UserPasswordParam userPasswordParam);
}
