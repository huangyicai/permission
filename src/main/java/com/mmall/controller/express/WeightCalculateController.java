package com.mmall.controller.express;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mmall.config.UserInfoConfig;
import com.mmall.dto.BillDto;
import com.mmall.model.Response.InfoEnums;
import com.mmall.model.Response.Result;
import com.mmall.model.SysUserInfo;
import com.mmall.model.Total;
import com.mmall.model.WeightCalculate;
import com.mmall.model.params.BillParam;
import com.mmall.service.SysUserInfoService;
import com.mmall.service.TotalService;
import com.mmall.service.WeightCalculateService;
import com.mmall.util.DateUtils;
import com.mmall.util.LevelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 重量区间计算表 前端控制器
 * </p>
 *
 * @author qty
 * @since 2018-09-25
 */
@Api(value = "WeightCalculateController", description = "重量区间计算表管理")
@RestController
@RequestMapping("/express/weightCalculate")
public class WeightCalculateController {

    @Autowired
    private WeightCalculateService weightCalculateService;

    @Autowired
    private TotalService totalService;

    @Autowired
    private SysUserInfoService sysUserInfoService;

    @ApiOperation(value = "获取重量区间占比",  notes="需要Authorization")
    @PostMapping(value = "/getWeightCalculate")
    public Result<Map<String,String>> getWeightCalculate(@RequestBody BillParam billParam){

        if(billParam.getUserId()==null||"".equals(billParam.getUserId())){
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

            billParam.setUserId(nameStr);
        }
        List<Total> list = totalService.listToal(billParam.getDate(),billParam.getUserId());
        if(list.size()<=0){
            return Result.error(InfoEnums.DATA_IS_NULL);
        }

        String totalIdStr="";

        for(Total total:list){
            totalIdStr+=total.getTotalId()+",";
        }

        totalIdStr=totalIdStr.substring(0,totalIdStr.length()-1);
        Map<String,String> weightCalculate = weightCalculateService.getWeightCalculate(totalIdStr);
        return Result.ok(weightCalculate);
    }
}

