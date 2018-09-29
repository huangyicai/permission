package com.mmall.service.serviceImpl;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.mmall.constants.LevelConstants;
import com.mmall.dao.*;
import com.mmall.dto.SysMenuDto;
import com.mmall.dto.SysUserInfoDto;
import com.mmall.model.Response.InfoEnums;
import com.mmall.model.Response.Result;
import com.mmall.model.*;
import com.mmall.model.params.UserInfoExpressParm;
import com.mmall.model.params.UserInfoServiceParm;
import com.mmall.model.params.UserPasswordParam;
import com.mmall.service.SysUserService;
import com.mmall.util.LevelUtil;
import com.mmall.util.SmsUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hyc
 * @since 2018-09-15
 */
@Service
@Slf4j
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {
    @Autowired
    private  SysUserMapper sysUserMapper;
    @Autowired
    private SysMenuMapper sysMenuMapper;
    @Autowired
    private SysUserInfoMapper sysUserInfoMapper;
    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;
    @Autowired
    private CodeRecordMapper codeRecordMapper;

    public Comparator<SysMenuDto> menusSeqComparator = new Comparator<SysMenuDto>() {
        public int compare(SysMenuDto o1, SysMenuDto o2) {
            return o1.getSeq() - o2.getSeq();
        }
    };

    public Result<List<SysMenuDto>> findAllMenuByUser(SysUserInfo user,Integer platId) {
        if(platId!=0&&platId!=null){
            if(!user.getPlatformId().equals(platId)){
                throw new UnauthorizedException();
            }
        }
        Integer id = user.getId();
        List<SysMenu> sysMenus = sysMenuMapper.findAllMenuByUser(id);
        List<SysMenuDto> dtoList = Lists.newArrayList();
        for (SysMenu sysMenu : sysMenus) {
            dtoList.add(SysMenuDto.adapt(sysMenu));
        }
        if (CollectionUtils.isEmpty(dtoList)) {
            return Result.ok(dtoList);
        }
        // level -> [aclmodule1, aclmodule2, ...] Map<String, List<Object>>
        Multimap<String, SysMenuDto> levelAclModuleMap = ArrayListMultimap.create();
        List<SysMenuDto> rootList = Lists.newArrayList();

        for (SysMenuDto dto : dtoList) {
            levelAclModuleMap.put(dto.getLevel(), dto);
            if (LevelUtil.ROOT.equals(dto.getLevel())) {
                rootList.add(dto);
            }
        }
        Collections.sort(rootList, menusSeqComparator);
        transformMenuTree(rootList, LevelUtil.ROOT, levelAclModuleMap);
        return Result.ok(rootList);
    }
    public void transformMenuTree(List<SysMenuDto> dtoList, String level, Multimap<String, SysMenuDto> levelAclModuleMap) {
        for (int i = 0; i < dtoList.size(); i++) {
            SysMenuDto dto = dtoList.get(i);
            String nextLevel = LevelUtil.calculateLevel(level, dto.getId());
            List<SysMenuDto> tempList = (List<SysMenuDto>) levelAclModuleMap.get(nextLevel);
            if (CollectionUtils.isNotEmpty(tempList)) {
                Collections.sort(tempList, menusSeqComparator);
                dto.setSysMenus(tempList);
                transformMenuTree(tempList, nextLevel, levelAclModuleMap);
            }
        }
    }




    public Result<AuthInfo<SysUserInfo>> login(String username, String password) {
        Subject subject = SecurityUtils.getSubject();
        SysUserInfo sysUserInfo = sysUserInfoMapper.findUserByusername(username);
        if(sysUserInfo==null){
            return Result.error(InfoEnums.USER_NOT_EXISTENCE);
        }
        if(sysUserInfo.getStatus()==0){
            return Result.error(InfoEnums.USER_NOT_FROZEN);
        }
        if(sysUserInfo.getStatus()==-1){
            return Result.error(InfoEnums.USER_NOT_EXISTENCE);
        }
        if(!subject.isAuthenticated()){
            AuthInfo<SysUserInfo> authInfo = new AuthInfo();
            //SysUser userByusername = sysUserMapper.findUserByusername(username);
            //SysUser userByusername =sysUserMapper
            if(sysUserInfo==null){
                return new Result(InfoEnums.ERROR);
            }
            UsernamePasswordToken token =null;
            token = new UsernamePasswordToken(username, password);
            try {
                subject.login(token);
                // 在session中存放用户信息
                subject.getSession().setAttribute("user", sysUserInfo);
                authInfo.setToken( subject.getSession().getId().toString());
                authInfo.setAuth(sysUserInfo);
            } catch (IncorrectCredentialsException e) {
                return Result.error(InfoEnums.PASSWORD_INCORRECT);
            } catch (LockedAccountException e) {
                return Result.error(InfoEnums.ERROR);
            } catch (AuthenticationException e) {
                return Result.error(InfoEnums.ERROR);
            } catch (Exception e) {
                e.printStackTrace();
            }
           return Result.ok(authInfo);
        };
        return Result.error(InfoEnums.AUTHORIZATION);
    }
    @Transactional
    public Result fnRegister(UserInfoExpressParm user,SysUserInfo parent) {
        return register(user,parent, LevelConstants.EXPRESS,2);
    }


    public  Result register(UserInfoExpressParm user,SysUserInfo parent,Integer platformId,Integer roleId){
        SysUser userByusername =sysUserMapper.selectOne(new QueryWrapper<SysUser>().eq("username",user.getUsername()));
        if(userByusername!=null){
            return Result.error(InfoEnums.USERNAME_EXISTENCE);
        }
        Md5Hash md = new Md5Hash(user.getPassword(),user.getUsername(),1024);
        SysUser sysUser = SysUser.builder().password(md.toString()).username(user.getUsername()).build();
        int insert = sysUserMapper.insert(sysUser);
        userByusername =sysUserMapper.selectOne(new QueryWrapper<SysUser>().eq("username",user.getUsername()));
        SysUserInfo userInfo = SysUserInfo.builder()
                .userId(userByusername.getId())
                .parentId(parent.getId())
                .name(user.getName())
                .email(user.getEmail())
                .companyName(user.getCompanyName())
                .province(user.getProvince())
                .city(user.getCity())
                .area(user.getArea())
                .address(user.getAddress())
                .telephone(user.getTelephone())
                .personInCharge(user.getPersonInCharge())
                .level(LevelUtil.calculateLevel(parent.getLevel(), parent.getId()))
                .platformId(platformId)
                .build();
        sysUserInfoMapper.insert(userInfo);
        userInfo =sysUserInfoMapper.selectOne(new QueryWrapper<SysUserInfo>().eq("user_id",userByusername.getId()));

        SysUserRole sysUserRole = new SysUserRole();
        sysUserRole.setRoleId(roleId);
        sysUserRole.setUserId(userInfo.getId());
        sysUserRoleMapper.insert(sysUserRole);
        return Result.ok();
    }

    public Result<List<SysUserInfo>> getCompanys(SysUserInfo user) {

        Integer id = user.getId();

        List<SysUserInfo> sysUserInfos2 = sysUserInfoMapper.selectList(new QueryWrapper<SysUserInfo>().eq("parent_id", id).notIn("status",-1));
        /*List<SysUserInfo> sysUserInfos = sysUserInfoMapper.findUserByid("%"+id+"%");
        String nextLevel = LevelUtil.calculateLevel(user.getLevel(), id);
        List<SysUserInfoDto> dtoList = Lists.newArrayList();
        for (SysUserInfo sysUserInfo : sysUserInfos) {
            dtoList.add(SysUserInfoDto.adapt(sysUserInfo));
        }
        if (CollectionUtils.isEmpty(dtoList)) {
            return Result.ok(dtoList);
        }
        // level -> [aclmodule1, aclmodule2, ...] Map<String, List<Object>>
        Multimap<String, SysUserInfoDto> levelMap = ArrayListMultimap.create();
        List<SysUserInfoDto> rootList = Lists.newArrayList();

        for (SysUserInfoDto dto : dtoList) {
            levelMap.put(dto.getLevel(), dto);
            if (nextLevel.equals(dto.getLevel())) {
                rootList.add(dto);
            }
        }
        transformUserInfoTree(rootList, nextLevel, levelMap);*/
        return Result.ok(sysUserInfos2);
    }

    public Result frozen(Integer id) {
        SysUserInfo userInfo = sysUserInfoMapper.selectById(id);
        if(userInfo.getStatus()==0){
            userInfo.setStatus(1);
        }
        else if(userInfo.getStatus()==1){
            userInfo.setStatus(0);
        }else {
            return Result.error(InfoEnums.USER_NOT_EXISTENCE);
        }
        sysUserInfoMapper.updateById(userInfo);
        return Result.ok();
    }

    public Result deleteUser(Integer id) {
        SysUserInfo userInfo = sysUserInfoMapper.selectById(id);

        if(userInfo.getStatus()==-1){
            return Result.error(InfoEnums.USER_NOT_EXISTENCE);
        }else {
            userInfo.setStatus(-1);
        }
        sysUserInfoMapper.updateById(userInfo);
        return Result.ok();
    }

    public Result updateExpress(UserInfoServiceParm user) {
        SysUserInfo sysUserInfo = sysUserInfoMapper.selectById(user.getId());
        if(sysUserInfo==null){
            return Result.error(InfoEnums.USERNAME_EXISTENCE);
        }
        sysUserInfo.setName(user.getName());
        sysUserInfo.setEmail(user.getEmail());
        sysUserInfo.setCompanyName(user.getCompanyName());
        sysUserInfo.setProvince(user.getProvince());
        sysUserInfo.setCity(user.getCity());
        sysUserInfo.setArea(user.getArea());
        sysUserInfo.setAddress(user.getAddress());
        sysUserInfo.setTelephone(user.getTelephone());
        sysUserInfo.setPersonInCharge(user.getPersonInCharge());
        sysUserInfoMapper.updateById(sysUserInfo);
        return Result.ok();
    }

    public Result<Map<String,Object>> getUserInfo(SysUserInfo user) {
        SysUser sysUser = sysUserMapper.selectById(user.getUserId());
        sysUser.setPassword("***********");
        user = sysUserInfoMapper.selectById(user.getId());
        Map<String,Object> map = Maps.newHashMap();
        map.put("user",sysUser);
        map.put("userInfo",user);
        return Result.ok(map);
    }

    public Result getCode(String phone) throws ClientException {
        Long today = new Date().getTime();
        String code = new StringBuilder(today.toString()).reverse().substring(0, 6);
        SendSmsResponse sendSmsResponse = SmsUtil.sendSms(phone, code);
        if("OK".equalsIgnoreCase(sendSmsResponse.getCode())){
            CodeRecord codeRecord = new CodeRecord();
            codeRecord.setCode(code);
            codeRecord.setPhone(phone);
            int insert = codeRecordMapper.insert(codeRecord);
            if(insert>0){
                return  Result.ok();
            }
            return  Result.error(InfoEnums.ERROR);
        }
        return Result.error(InfoEnums.PHONE_ERROR);
    }

    public Result updateUserPassword(SysUserInfo user, String code, UserPasswordParam userPasswordParam) {
        List<CodeRecord> allByPhone = codeRecordMapper.selectList(new QueryWrapper<CodeRecord>()
                .eq("phone",user.getTelephone())
                .orderByDesc("create_time"));
        if (allByPhone == null || allByPhone.size() == 0 || !allByPhone.get(0).getCode().equals(code)) {
            return Result.error(InfoEnums.VERIFY_FAIL);
        }
        if(!userPasswordParam.getOnePassword().equals(userPasswordParam.getTwoPassword())){
            return Result.error(InfoEnums.PASSWORD_ATYPISM);
        }
        SysUser sysUser = sysUserMapper.selectById(user.getUserId());
        Md5Hash md = new Md5Hash(userPasswordParam.getOnePassword(),sysUser.getUsername(),1024);
        sysUser.setPassword(md.toString());
        sysUserMapper.updateById(sysUser);
        return Result.ok();
    }


}
