package com.mmall.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.mmall.dao.BillKeywordMapper;
import com.mmall.dao.SysMenuMapper;
import com.mmall.dao.SysUserInfoMapper;
import com.mmall.dao.SysUserMapper;
import com.mmall.dto.SysUserInfoDto;
import com.mmall.model.BillKeyword;
import com.mmall.model.Response.InfoEnums;
import com.mmall.model.Response.Result;
import com.mmall.model.SysUserInfo;
import com.mmall.model.params.UserInfoExpressParm;
import com.mmall.service.ExpressUserService;
import com.mmall.util.BeanValidator;
import com.mmall.util.LevelUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpressUserServicelmpl implements ExpressUserService {

    @Autowired
    private SysUserInfoMapper sysUserInfoMapper;
    @Autowired
    private SysUserServiceImpl sysUserService;
    @Autowired
    private BillKeywordMapper billKeywordMapper;

    public Result expressRegister(UserInfoExpressParm user, SysUserInfo parent,Integer id,Integer level) {
        if(id!=0) parent= sysUserInfoMapper.selectById(id);;

        if(level==1){
            BeanValidator.check(user);
           return sysUserService.register(user,parent,3,5);
        }
        SysUserInfo userInfo = SysUserInfo.builder()
                .parentId(parent.getId())
                .name(user.getName())
                .personInCharge(user.getPersonInCharge())
                .level(LevelUtil.calculateLevel(parent.getLevel(), parent.getId()))
                .platformId(-1)
                .build();
        sysUserInfoMapper.insert(userInfo);
        return Result.ok();
    }

    public Result<List<SysUserInfoDto>> getCusmoters(SysUserInfo user) {

        Integer id = user.getId();
        //List<SysUserInfo> sysUserInfos2 = sysUserInfoMapper.selectList(new QueryWrapper<SysUserInfo>().eq("parent_id", id).notIn("status",-1));
        List<SysUserInfo> sysUserInfos = sysUserInfoMapper.selectList(new QueryWrapper<SysUserInfo>()
                .like("level","%"+id+"%").eq("status",1).in("platform_id",-1,3));
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
        transformUserInfoTree(rootList, nextLevel, levelMap);
        return Result.ok(rootList);
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

    public Result saveKeyword(BillKeyword billKeywords) {
        billKeywordMapper.insert(billKeywords);
        return Result.ok();
    }

    public Result<List<BillKeyword>> findAllKeywordById(Integer id) {
        List<BillKeyword> billKeywords = billKeywordMapper.selectList(new QueryWrapper<BillKeyword>().eq("user_id", id).eq("status",1));
        return Result.ok(billKeywords);
    }

    public Result deleteKeywordBykeyId(Integer keyId) {
        BillKeyword billKeyword = billKeywordMapper.selectById(keyId);
        billKeyword.setStatus(0);
        billKeywordMapper.updateById(billKeyword);
        return Result.ok();
    }

    public Result<SysUserInfo> getCusmotersInfo(Integer id) {
        SysUserInfo sysUserInfo = sysUserInfoMapper.selectById(id);
        return Result.ok(sysUserInfo);
    }

    public  void transformUserInfoTree(List<SysUserInfoDto> dtoList, String level, Multimap<String, SysUserInfoDto> levelMap) {
        for (int i = 0; i < dtoList.size(); i++) {
            SysUserInfoDto dto = dtoList.get(i);
            String nextLevel = LevelUtil.calculateLevel(level, dto.getId());
            List<SysUserInfoDto> tempList = (List<SysUserInfoDto>) levelMap.get(nextLevel);
            if (CollectionUtils.isNotEmpty(tempList)) {
                dto.setSysUserInfos(tempList);
                transformUserInfoTree(tempList, nextLevel, levelMap);
            }
        }
    }
}
