package com.mmall.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mmall.config.UserInfoConfig;
import com.mmall.dao.WeightCalculateMapper;
import com.mmall.model.Response.InfoEnums;
import com.mmall.model.Response.Result;
import com.mmall.model.SysUserInfo;
import com.mmall.model.Total;
import com.mmall.model.WeightCalculate;
import com.mmall.model.params.BillParam;
import com.mmall.service.SysUserInfoService;
import com.mmall.service.TotalService;
import com.mmall.service.WeightCalculateService;
import com.mmall.util.LevelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 重量区间计算表 服务实现类
 * </p>
 *
 * @author qty
 * @since 2018-09-25
 */
@Service
public class WeightCalculateServiceImpl extends ServiceImpl<WeightCalculateMapper, WeightCalculate> implements WeightCalculateService {

    @Autowired
    private WeightCalculateMapper weightCalculateMapper;

    @Autowired
    private TotalService totalService;

    @Autowired
    private SysUserInfoService sysUserInfoService;
    /**
     * 获取重量区间的统计
     * @param billParam
     * @return
     */
    public Map<String,String> getWeightCalculate(BillParam billParam) {

        if(billParam.getUserId()==null||"".equals(billParam.getUserId())){
            billParam.setUserId(totalService.getUserIdStr());
        }

        //定义反射实体
        WeightCalculate weightCalculate;

        //定义循环判断的规则
//        Double[] interval={0.01,0.5,1.0,2.0,3.0,4.0,5.0,6.0,7.0,8.0,9.0,10.0,11.0,12.0,13.0,14.0,15.0,16.0,17.0,18.0,19.0,20.0};
        Double[] interval={0.01,0.5,1.0,2.0,3.0,4.0,5.0,6.0,7.0,8.0,9.0,10.0};

        List<Total> list = totalService.listToal(billParam.getDate(),billParam.getUserId());
        if(list.size()<=0){
            weightCalculate=new WeightCalculate();
        }else{
            String totalIdStr="";

            for(Total total:list){
                totalIdStr+=total.getTotalId()+",";
            }

            totalIdStr=totalIdStr.substring(0,totalIdStr.length()-1);


            weightCalculate = weightCalculateMapper.getWeightCalculate(totalIdStr);
        }

        Field fields[]=weightCalculate.getClass().getDeclaredFields();
        String[] name=new String[fields.length];
        Object[] value=new Object[fields.length];

        Map<String,String> map= new HashMap<String, String>();
        try {
            Field.setAccessible(fields, true);
            for (int i = 3; i < value.length; i++) {
                if(i>14){
                    break;
                }
                name[i] = fields[i].getName();
                value[i] = fields[i].get(weightCalculate);

                if(i==3){
                    map.put("10以上",value[i]==null?"0":value[i].toString());
                    continue;
                }

                map.put(interval[i-4]+"到"+interval[i-3],value[i]==null?"0.00":value[i].toString());
//                System.out.println(interval[i-4]+"到"+interval[i-3]+"-------"+name[i]+"-------"+value[i]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return map;
    }
}
