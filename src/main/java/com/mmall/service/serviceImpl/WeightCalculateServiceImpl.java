package com.mmall.service.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mmall.constants.LevelConstants;
import com.mmall.dao.WeightCalculateMapper;
import com.mmall.dto.WeightCalculateDto;
import com.mmall.model.Total;
import com.mmall.model.WeightCalculate;
import com.mmall.model.params.BillParam;
import com.mmall.service.SysUserInfoService;
import com.mmall.service.TotalService;
import com.mmall.service.WeightCalculateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

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
    public List<WeightCalculateDto> getWeightCalculate(BillParam billParam) {

        if(billParam.getUserId()==null||"".equals(billParam.getUserId())){
            billParam.setUserId(totalService.getUserIdStr());
        }

        //获取当月账单
        List<Total> list = totalService.listToal(billParam.getDate(),billParam.getUserId());

        //定义循环判断的规则
        Double[] interval= LevelConstants.INTERVAL;

        //初始化数据
        List<WeightCalculateDto> weightCalculateDto=new ArrayList<>();
        for (int i = 0; i <interval.length; i++) {
            WeightCalculateDto w=new WeightCalculateDto();
            if(i==interval.length-1){
                w.setInterval("10以上");
                w.setWeight(0.0);
            }else{
                w.setInterval(interval[i]+"到"+interval[i+1]);
                w.setWeight(0.0);
            }
            weightCalculateDto.add(w);
        }

        //空值处理
        if(list==null || list.size()<=0){
            return weightCalculateDto;
        }

        String totalIdStr="";
        for(Total total:list){
            totalIdStr+=total.getTotalId()+",";
        }
        totalIdStr=totalIdStr.substring(0,totalIdStr.length()-1);

        //获取重量区间数据
        WeightCalculate weightCalculate = weightCalculateMapper.getWeightCalculate(totalIdStr);

        //反射获取的数据
        Field fields[]=weightCalculate.getClass().getDeclaredFields();
        Object[] value=new Object[fields.length];

        try {
            Field.setAccessible(fields, true);
            for (int i = 3; i < value.length; i++) {
                if(i>15){
                    break;
                }
                value[i] = fields[i].get(weightCalculate);

                if(i==3){
                    WeightCalculateDto w = weightCalculateDto.get(weightCalculateDto.size()-1);
                    w.setWeight(Double.valueOf(value[i].toString()));
                }else{
                    WeightCalculateDto w = weightCalculateDto.get(i-4);
                    w.setWeight(Double.valueOf(value[i].toString()));
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return weightCalculateDto;
    }
}
