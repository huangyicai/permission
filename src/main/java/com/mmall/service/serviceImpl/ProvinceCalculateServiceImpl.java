package com.mmall.service.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mmall.dao.ProvinceCalculateMapper;
import com.mmall.model.ProvinceCalculate;
import com.mmall.service.ProvinceCalculateService;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 省计表 服务实现类
 * </p>
 *
 * @author qty
 * @since 2018-09-25
 */
@Service
public class ProvinceCalculateServiceImpl extends ServiceImpl<ProvinceCalculateMapper, ProvinceCalculate> implements ProvinceCalculateService {

    @Autowired
    private ProvinceCalculateMapper provinceCalculateMapper;

    /**
     * 获取省计数据分析
     * @param totalId
     * @return
     */
    public Map<String,String> getProvinceCalculate(String totalId) {

        String[] proStr={"北京","天津","河北","山西","内蒙古","辽宁","吉林","黑龙江","上海","江苏","浙江","安徽","福建",
                "江西","山东","河南","湖北","湖南","广东","广西","海南","重庆","四川","贵州","云南","西藏","陕西","甘肃",
                "青海","宁夏","新疆","台湾","香港","澳门"};

        Map<String,String> map= new HashMap<String, String>();
        ProvinceCalculate provinceCalculate = provinceCalculateMapper.getProvinceCalculate(totalId);
        Field fields[]=provinceCalculate.getClass().getDeclaredFields();
        String[] name=new String[fields.length];
        Object[] value=new Object[fields.length];

        try {
            Field.setAccessible(fields, true);
            for (int i = 3; i < value.length; i++) {
                name[i] = fields[i].getName();
                value[i] = fields[i].get(provinceCalculate);
                map.put(proStr[i-3],value[i].toString());
//                System.out.println(name[i]+"-----------"+proStr[i-3]+"--------------"+value[i]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
}
