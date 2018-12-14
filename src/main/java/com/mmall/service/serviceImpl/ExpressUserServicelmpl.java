package com.mmall.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.*;
import com.mmall.component.ApplicationContextHelper;
import com.mmall.config.UserInfoConfig;
import com.mmall.constants.LevelConstants;
import com.mmall.dao.*;
import com.mmall.dto.SysUserInfoDto;
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
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private FnContactsMapper fnContactsMapper;

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
                .courierId(parent.getCourierId())
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
    public Result importUser(MultipartFile file,SysUserInfo parent, Integer id) throws IOException{

        if(id!=0) parent= sysUserInfoMapper.selectById(id);

        ReadExcel re=new ReadExcel();
        Map<String, List> stringListMap = re.readExcel(file);

        List list = stringListMap.get("listError");
        if(list.size()!=0){
            return Result.error(InfoEnums.TABLE_FORMAT_ERROR,list);
        }

        List<UserInfoExpressParm> userInfoExpressParms = stringListMap.get("list");

        //执行
        for (UserInfoExpressParm ui:userInfoExpressParms){
            Result register = sysUserService.register(ui, parent, LevelConstants.SERVICE, 5);
            if(register.getCode()!=0){
                return register;
            }
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
    @Transactional
    public Result deleteUser(Integer id) {
        SysUserInfo user = UserInfoConfig.getUserInfo();
        SysUserInfo userInfo = sysUserInfoMapper.selectById(id);
        Integer expressId = userInfo.getParentId();
        if(user.getId()==expressId||user.getId().equals(expressId)){
            return Result.error(InfoEnums.BRANCH_NOTDELETE);
        }
        if(userInfo.getStatus()==-1){
            return Result.error(InfoEnums.USER_NOT_EXISTENCE);
        }else {
            userInfo.setStatus(-1);
        }

        sysUserInfoMapper.updateById(userInfo);
        sysUserInfoMapper.updateUserList("%."+id+"%");
        sysUserMapper.deleteById(userInfo.getUserId());
        return Result.ok();
    }
    public Result saveKeyword(BillKeyword billKeywords) {

        /*List<BillKeyword> key_names = billKeywordMapper.selectList(new QueryWrapper<BillKeyword>()
                .eq("keyword", billKeywords.getKeyword()).eq("status",1));
        Set<Integer> set = Sets.newHashSet();
        SysUserInfo sysUserInfo1 = sysUserInfoMapper.selectById(billKeywords.getUserId());
        Integer a = Integer.parseInt(sysUserInfo1.getLevel().split("\\.")[2]);
        set.add(a);
        for (BillKeyword sk:key_names) {
            SysUserInfo sysUserInfo = sysUserInfoMapper.selectById(sk.getUserId());
            Integer s = Integer.parseInt(sysUserInfo.getLevel().split("\\.")[2]);
            boolean add = set.add(s);
            if(!add){
                return Result.error(InfoEnums.ERROR,sysUserInfo.getName()+"账户中存在此商户名");
            }
        }*/
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

    public Result getCusmotersInfo(Integer id) {
        SysUserInfo user = sysUserInfoMapper.selectById(id);
        SysUser sysUser = sysUserMapper.selectById(user.getUserId());
        sysUser.setPassword("***********");

        Map<String,Object> map = Maps.newHashMap();
        map.put("user",sysUser);
        map.put("userInfo",user);
        return Result.ok(map);
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
        for (SysUserInfo su:sysUserInfos){
            SysUser sysUser = sysUserMapper.selectById(su.getUserId());
            su.setCompanyName(sysUser.getUsername());
        }
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

    @Override
    public Result getFnContacts(SysUserInfo userInfo) {
        FnContacts fnContacts = fnContactsMapper.getOneFnContacts(userInfo.getId());
        if(fnContacts==null){
            fnContacts = fnContactsMapper.selectOne(new QueryWrapper<FnContacts>().eq("id",1));
            if(fnContacts==null){
                List<FnContacts> fnContactsList = fnContactsMapper.selectList(new QueryWrapper<FnContacts>());
                if(fnContactsList.isEmpty()){
                    fnContacts = new FnContacts(1,"黄益财","18069000780");
                    fnContactsMapper.insert(fnContacts);
                }
            }
        }
        return Result.ok(fnContacts);
    }

    @Override
    public Result expressUpdateInfo(UserInfoExpressParm user) {
        SysUserInfo sysUserInfo = sysUserInfoMapper.selectById(user.getId());
        sysUserInfo.setCompanyName(user.getCompanyName());

        sysUserInfo.setProvince(user.getProvince());
        sysUserInfo.setCity(user.getCity());
        sysUserInfo.setArea(user.getArea());

        sysUserInfo.setAddress(user.getAddress());
        sysUserInfo.setName(user.getName());
        sysUserInfo.setEmail(user.getEmail());
        sysUserInfo.setPersonInCharge(user.getPersonInCharge());
        sysUserInfo.setTelephone(user.getTelephone());
        sysUserInfoMapper.updateById(sysUserInfo);
        return Result.ok();
    }

    @Override
    public Result passwordReset(Integer id) {
        Integer userId = sysUserInfoMapper.selectById(id).getUserId();
        SysUser sysUser = sysUserMapper.selectById(userId);
        Md5Hash md = new Md5Hash("123456",sysUser.getUsername(),1024);
        sysUser.setPassword(md.toString());
        sysUserMapper.updateById(sysUser);
        return Result.ok();
    }

    @Override
    public Result<List<SysUserInfoDto>> getCusmotersBranch(SysUserInfo user) {

        Integer id = user.getId();
        //List<SysUserInfo> sysUserInfos2 = sysUserInfoMapper.selectList(new QueryWrapper<SysUserInfo>().eq("parent_id", id).notIn("status",-1));
        List<SysUserInfo> sysUserInfos = sysUserInfoMapper.selectList(new QueryWrapper<SysUserInfo>()
                .like("level","%"+id+"%")
                .in("status",1,0)
                .eq("platform_id",LevelConstants.BRANCH));
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

    @Override
    public Result<List<SysUserInfo>> getBranchCusmotersUser(Integer id) {
        List<SysUserInfo> sysUserInfos = sysUserInfoMapper.selectList(new QueryWrapper<SysUserInfo>()
                .eq("parent_id",id)
                .in("status",1,0)
                .eq("platform_id",LevelConstants.SERVICE));
        return Result.ok(sysUserInfos);
    }

    @Override
    public Result<List<SysUserInfo>> transferBranchCusmotersUser(Integer toBranchId, String userIds) {
        SysUserInfo sysUserInfo = sysUserInfoMapper.selectById(toBranchId);
        String level = sysUserInfo.getLevel()+"."+toBranchId;
        int i = sysUserInfoMapper.updataUserLevelAndParentId(userIds, level, toBranchId);
        if(i==userIds.split(",").length-1){
            return Result.ok();
        }
        return Result.error(InfoEnums.ERROR);
    }

    public static void main(String[] args) {
        String s = ",1,2";
        System.out.println(s.split(",").length);
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
