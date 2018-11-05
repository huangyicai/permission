package com.mmall.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.mmall.constants.LevelConstants;
import com.mmall.dao.*;
import com.mmall.dto.SysUserInfoDto;
import com.mmall.excel.thread.ThreadInsert;
import com.mmall.model.*;
import com.mmall.model.Response.InfoEnums;
import com.mmall.model.Response.Result;
import com.mmall.model.params.UserInfoExpressParm;
import com.mmall.model.params.UserInfoOperateParam;
import com.mmall.service.ExpressUserService;
import com.mmall.util.BeanValidator;
import com.mmall.util.LevelUtil;
import com.mmall.util.ReadExcel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@Slf4j
public class ExpressUserServicelmpl implements ExpressUserService {

    @Autowired
    private SysUserInfoMapper sysUserInfoMapper;
    @Autowired
    private SysUserServiceImpl sysUserService;
    @Autowired
    private BillKeywordMapper billKeywordMapper;
    @Autowired
    private PaymentMethodMapper paymentMethodMapper;

    public Result expressRegister(UserInfoExpressParm user, SysUserInfo parent,Integer id,Integer level) {

        if(id!=0) parent= sysUserInfoMapper.selectById(id);
        if(level==1){
            BeanValidator.check(user);
            PaymentMethod paymentMethod = paymentMethodMapper.selectOne(new QueryWrapper<PaymentMethod>().eq("user_id", parent.getId()));
            if(paymentMethod==null){
                return Result.error(InfoEnums.ADD_PAYMENT_INSTITUTION);
            }
            return sysUserService.register(user,parent, LevelConstants.SERVICE,5);
        }
        SysUserInfo userInfo = SysUserInfo.builder()
                .parentId(parent.getId())
                .name(user.getName())
                .personInCharge(user.getPersonInCharge())
                .level(LevelUtil.calculateLevel(parent.getLevel(), parent.getId()))
                .platformId(-1)
                .build();
        sysUserInfoMapper.insert(userInfo);
        PaymentMethod paymentMethod = PaymentMethod.builder()
                .userId(userInfo.getId())
                .payee(user.getPayee())
                .paymentAccount(user.getPaymentAccount())
                .typeName(user.getTypeName()).build();
        paymentMethodMapper.insert(paymentMethod);
        return Result.ok();
    }

    @Override
    public Result importUser(MultipartFile file,SysUserInfo parent, Integer id) throws IOException, InterruptedException {

        if(id!=0) parent= sysUserInfoMapper.selectById(id);
        ReadExcel re=new ReadExcel();

        List<UserInfoExpressParm> userInfoExpressParms = re.readExcel(file);

        //创建线程池
        ExecutorService threadPool = Executors.newFixedThreadPool(30);

        //执行
        for (UserInfoExpressParm ui:userInfoExpressParms){
            ThreadInsert ti=new ThreadInsert(ui,parent,id);
            threadPool.submit(ti);
        }

        //判断线程是否执行完毕
        threadPool.shutdown();
        while (true){
            if (threadPool.isTerminated()) {
                log.info("线程已执行完毕");
                break;
            }
            Thread.sleep(200);
        }

        return Result.ok();
    }

    public Result<List<SysUserInfoDto>> getCusmoters(SysUserInfo user) {

        Integer id = user.getId();
        //List<SysUserInfo> sysUserInfos2 = sysUserInfoMapper.selectList(new QueryWrapper<SysUserInfo>().eq("parent_id", id).notIn("status",-1));
        List<SysUserInfo> sysUserInfos = sysUserInfoMapper.selectList(new QueryWrapper<SysUserInfo>()
                .like("level","%"+id+"%")
                .in("status",1,0)
                .in("platform_id",LevelConstants.BRANCH,LevelConstants.SERVICE));
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
        Integer id = billKeywordMapper.insertBillKeyword(billKeywords);
        return Result.ok(billKeywords);
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

    public Result registerOperate(UserInfoOperateParam user, Integer level, SysUserInfo parent) {
        UserInfoExpressParm userExpress = new UserInfoExpressParm();
        userExpress.setUsername(user.getUsername());
        userExpress.setPassword(user.getPassword());
        userExpress.setName(user.getName());
        userExpress.setEmail(user.getEmail());
        userExpress.setTelephone(user.getTelephone());
        userExpress.setPersonInCharge(user.getPersonInCharge());

        userExpress.setProvince(parent.getProvince());
        userExpress.setCity(parent.getCity());
        userExpress.setArea(parent.getArea());
        userExpress.setAddress(parent.getAddress());
        userExpress.setCompanyName(parent.getCompanyName());

        return sysUserService.register(
                userExpress,
                parent,
                LevelConstants.EXPRESS,
                level==1?LevelConstants.OPERATE:LevelConstants.SERVICE_PHONE);
    }

    public Result getAllOperate(SysUserInfo userInfo) {
        List<SysUserInfo> sysUserInfos = sysUserInfoMapper.selectList(new QueryWrapper<SysUserInfo>()
                .eq("parent_id", userInfo.getId())
                .eq("platform_id",LevelConstants.EXPRESS)
                .in("status",1,0));
        Multimap<String, SysUserInfo> userMap = ArrayListMultimap.create();
        String userService = "userService";//客服
        String userOperate = "userOperate";//运营
        for (SysUserInfo sui:sysUserInfos){
            //SysUserRole sysUserRole = sysUserRoleMapper.selectOne(new QueryWrapper<SysUserRole>().eq("user_id", sui.getId()));
            userMap.put(sui.getDisplay()==2?userOperate:userService,sui);
        }
        Map<String,List<SysUserInfo>> map = Maps.newHashMap();
        //客服集合
        List<SysUserInfo> userServiceList = (List<SysUserInfo>) userMap.get(userService);
        //运营集合
        List<SysUserInfo> userOperateList = (List<SysUserInfo>) userMap.get(userOperate);

        map.put(userService,userServiceList);
        map.put(userOperate,userOperateList);
        return Result.ok(map);
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
