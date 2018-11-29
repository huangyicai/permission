package com.mmall.service.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mmall.config.UserInfoConfig;
import com.mmall.dao.PricingGroupMapper;
import com.mmall.dao.SysUserInfoMapper;
import com.mmall.model.Response.InfoEnums;
import com.mmall.model.Response.Result;
import com.mmall.model.SysUserInfo;
import com.mmall.service.SysUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hyc
 * @since 2018-09-20
 */
@Service
public class SysUserInfoServiceImpl extends ServiceImpl<SysUserInfoMapper, SysUserInfo> implements SysUserInfoService {

    @Autowired
    private SysUserInfoService sysUserInfoService;

    @Autowired
    private PricingGroupMapper pricingGroupMapper;

    /**
     * 成本定价开关
     * @return
     */
    @Override
    public Result updatePricingStatus() {
        Integer state=0;
        SysUserInfo userInfo = UserInfoConfig.getUserInfo();
        SysUserInfo byId = sysUserInfoService.getById(userInfo.getId());
        state=(byId.getPricingStatus()==0 || byId.getPricingStatus()==null?1:0);

        if(state==1){

            //校验成本表是否存在数据
            List<Integer> ag = pricingGroupMapper.getAllPricingGroups(userInfo.getId());
            if(ag.size()!=34){
                return Result.error(InfoEnums.COST_IS_NULL);
            }

        }

        byId.setPricingStatus(state);
        boolean b = sysUserInfoService.updateById(byId);
        if(b){
            return Result.ok(byId);
        }
        return Result.error(InfoEnums.ERROR);
    }
}
