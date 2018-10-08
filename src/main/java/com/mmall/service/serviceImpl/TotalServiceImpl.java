package com.mmall.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mmall.config.UserInfoConfig;
import com.mmall.dao.TotalMapper;
import com.mmall.dto.BillDto;
import com.mmall.dto.ProfitsDto;
import com.mmall.model.Response.InfoEnums;
import com.mmall.model.Response.Result;
import com.mmall.model.SysUserInfo;
import com.mmall.model.Total;
import com.mmall.model.params.BillDetailsParam;
import com.mmall.model.params.BillParam;
import com.mmall.model.params.TotalIncomeParam;
import com.mmall.service.SysUserInfoService;
import com.mmall.service.TotalService;
import com.mmall.util.DateUtils;
import com.mmall.util.LevelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * <p>
 * 月计表(客户账单) 服务实现类
 * </p>
 *
 * @author qty
 * @since 2018-09-25
 */
@Service
public class TotalServiceImpl extends ServiceImpl<TotalMapper, Total> implements TotalService {

    @Autowired
    private TotalMapper totalMapper;

    @Autowired
    private SysUserInfoService sysUserInfoService;

    /**
     * 获取客户月计统计
     * @return
     */
    public BillDto getBillData(BillParam billParam) {

        if(billParam.getUserId()==null||"".equals(billParam.getUserId())){
            billParam.setUserId(getUserIdStr());
        }

        Total one = totalMapper.getToal(billParam.getDate(), billParam.getUserId());

        if(one==null){
            return null;
        }
        BillDto billDto=new BillDto();
        billDto.setTotalNumber(one.getTotalNumber());
        billDto.setTotalWeight(one.getTotalWeight());
        billDto.setAverageWeight(one.getTotalWeight().divide(new BigDecimal(one.getTotalNumber()),2, RoundingMode.DOWN));

        int days = DateUtils.getDays(one.getTotalTime());
        billDto.setDailyNum(one.getTotalNumber()/days);

        return billDto;
    }



    /**
     * 获取客户对应的账单
     * @param totalTime
     * @param userId
     * @return
     */
    public List<Total> listToal(String totalTime, String userId) {
        return totalMapper.listToal(totalTime,userId);
    }

    /**
     * 获取公司账单----数据分析
     * @return
     */
    public List<Total> getBill(Page<Total> page, BillDetailsParam billDetailsParam) {
        String nameStr=getUserIdStr();

        if(!"".equals(billDetailsParam.getUserId()) && billDetailsParam.getUserId()!=null){
            nameStr=billDetailsParam.getUserId();
        }

        page.setCurrent(billDetailsParam.getCurrent());
        page.setSize(billDetailsParam.getSize());
        List<Total> bill = totalMapper.getBill(billDetailsParam.getDate(),nameStr, billDetailsParam.getState());
        return bill;
    }

    /**
     * 获取已收和未收金额
     * @return
     */
    public TotalIncomeParam getBillCount(BillDetailsParam billDetailsParam) {

        String nameStr=getUserIdStr();

        String staStr="1,2,3";
        String steStr="4";
        Total totalPaid = totalMapper.getBillCount(billDetailsParam.getDate(), nameStr, steStr);
        Total totalOffer = totalMapper.getBillCount(billDetailsParam.getDate(), nameStr, staStr);

        TotalIncomeParam totalIncomeParam=new TotalIncomeParam();
        if(totalPaid!=null && totalPaid.getTotalPaid()!=null){
            totalIncomeParam.setTotalPaid(totalPaid.getTotalPaid());
        }
        if(totalOffer!=null && totalOffer.getTotalOffer()!=null){
            totalIncomeParam.setTotalOffer(totalOffer.getTotalOffer());
        }
        return totalIncomeParam;
    }

    /**
     * 利润分析
     * @param billParam
     * @return
     */
    public ProfitsDto getProfits(BillParam billParam) {

        if(billParam.getUserId()==null||"".equals(billParam.getUserId())){
            billParam.setUserId(getUserIdStr());
        }

        Total one = totalMapper.getToal( billParam.getDate(), billParam.getUserId());

        if(one==null){
            return null;
        }

        ProfitsDto billDto=new ProfitsDto();
        billDto.setTotalNumber(one.getTotalNumber());
        billDto.setTotalWeight(one.getTotalWeight());
        billDto.setAverageWeight(one.getTotalWeight().divide(new BigDecimal(one.getTotalNumber()),2, RoundingMode.DOWN));
        billDto.setTotalOffer(one.getTotalOffer());
        billDto.setTotalPaid(one.getTotalPaid());
        billDto.setTotalCost(one.getTotalCost());
        billDto.setProfits(one.getTotalPaid().subtract(one.getTotalCost()));
        billDto.setPrice(one.getTotalOffer().divide(new BigDecimal(one.getTotalNumber()),2,RoundingMode.DOWN));
        billDto.setCostPrice(one.getTotalCost().divide(new BigDecimal(one.getTotalNumber()),2,RoundingMode.DOWN));


        int days = DateUtils.getDays(one.getTotalTime());
        billDto.setAverageNumber(one.getTotalNumber()/days);
        return billDto;
    }

    /**
     * 根据当前登录用户，获取该公司下所有用户
     * @return
     */
    public String getUserIdStr(){
        SysUserInfo user = UserInfoConfig.getUserInfo();
        String s = LevelUtil.calculateLevel(user.getLevel(), user.getId());
        List<SysUserInfo> list1 = sysUserInfoService.list(new QueryWrapper<SysUserInfo>()
                .like("level", s)
                .eq("platform_id", 3)
                .select("id"));

        String nameStr="";
        for(SysUserInfo sysUserInfo: list1){
            nameStr+=sysUserInfo.getId()+",";
        }

        nameStr=nameStr.substring(0,nameStr.length()-1);
        return nameStr;
    }
}
