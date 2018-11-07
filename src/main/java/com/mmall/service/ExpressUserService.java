package com.mmall.service;

import com.mmall.dto.SysUserInfoDto;
import com.mmall.model.BillKeyword;
import com.mmall.model.Response.Result;
import com.mmall.model.SysUserInfo;
import com.mmall.model.params.UserInfoExpressParm;
import com.mmall.model.params.UserInfoOperateParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface ExpressUserService {

    /**
     * exress注册
     * @param user
     * @param parent
     * @return
     */
    Result expressRegister(UserInfoExpressParm user,SysUserInfo parent,Integer id,Integer level);

    /**
     * exress注册(导入)
     * @param parent
     * @return
     */
    Result importUser(MultipartFile file,SysUserInfo parent, Integer id) throws IOException, InterruptedException;

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
    Result getCusmotersInfo(Integer id);

    /**
     * 快递公司注册(1=运营号,2=客服)
     * @param user
     * @param level
     * @return
     */
    Result registerOperate(UserInfoOperateParam user, Integer level, SysUserInfo parent);

    /**
     * 获取快递公司=运营号/客服号)
     * @param userInfo
     * @return
     */
    Result getAllOperate(SysUserInfo userInfo);

    /**
     * 获取弗恩客服负责人
     * @param userInfo
     * @return
     */
    Result getFnContacts(SysUserInfo userInfo);
}
