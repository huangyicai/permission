package com.mmall.service.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mmall.dao.WeightCalculateMapper;
import com.mmall.model.WeightCalculate;
import com.mmall.service.WeightCalculateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.HashMap;
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

    /**
     * 获取重量区间的统计
     * @param totalId
     * @return
     */
    public Map<String,String> getWeightCalculate(String totalId) {

        Double[] interval={0.01,0.5,1.0,2.0,3.0,4.0,5.0,6.0,7.0,8.0,9.0,10.0,11.0,12.0,13.0,14.0,15.0,16.0,17.0,18.0,19.0,20.0};

        WeightCalculate weightCalculate = weightCalculateMapper.getWeightCalculate(totalId);

        Field fields[]=weightCalculate.getClass().getDeclaredFields();
        String[] name=new String[fields.length];
        Object[] value=new Object[fields.length];

        Map<String,String> map= new HashMap<String, String>();
        try {
            Field.setAccessible(fields, true);
            for (int i = 3; i < value.length; i++) {
                name[i] = fields[i].getName();
                value[i] = fields[i].get(weightCalculate);

                if(i==3){
                    map.put("20以上",value[i].toString());
                    continue;
                }

                map.put(interval[i-4]+"到"+interval[i-3],value[i].toString());
//                System.out.println(interval[i-4]+"到"+interval[i-3]+"-------"+name[i]+"-------"+value[i]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return map;
    }
}
