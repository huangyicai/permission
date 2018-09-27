package com.mmall.service;

import com.mmall.dto.SysUserInfoDto;
import com.mmall.model.BillKeyword;
import com.mmall.model.Response.Result;
import com.mmall.model.SysUserInfo;
import com.mmall.model.params.UserInfoExpressParm;

import java.util.List;

public interface ExpressUserService {

    /**
     * exress注册
     * @param user
     * @param parent
     * @return
     */
    Result expressRegister(UserInfoExpressParm user,SysUserInfo parent,Integer id,Integer level);

    /**
     * 获取用户列表
     * @param user
     * @return
     */
    Result<List<SysUserInfoDto>> getCusmoters(SysUserInfo user);

    /**
     * 冻结客户
     * @param id
     * @return
     */
    Result frozen(Integer id);

    /**
     * 删除客户账号
     * @param id
     * @return
     */
    Result deleteUser(Integer id);

    /**
     * 添加客户商户名
     * @return
     */
    Result saveKeyword(BillKeyword billKeywords);

    /**
     * 获取客户商户名
     * @param id
     * @return
     */
    Result<List<BillKeyword>> findAllKeywordById(Integer id);

    /**
     * 删除客户商户名
     * @param keyId
     * @return
     */
    Result deleteKeywordBykeyId(Integer keyId);

    /**
     * 获取用户信息
     * @param id
     * @return
     */
    Result<SysUserInfo> getCusmotersInfo(Integer id);
}
