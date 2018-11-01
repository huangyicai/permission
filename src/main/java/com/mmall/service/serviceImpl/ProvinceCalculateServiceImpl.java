package com.mmall.service.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mmall.constants.LevelConstants;
import com.mmall.dao.ProvinceCalculateMapper;
import com.mmall.dao.ProvincialMeterMapper;
import com.mmall.dto.DailyTotalDto;
import com.mmall.model.DailyTotal;
import com.mmall.model.ProvinceCalculate;
import com.mmall.model.ProvincialMeter;
import com.mmall.model.Total;
import com.mmall.model.params.BillParam;
import com.mmall.service.ProvinceCalculateService;
import com.mmall.service.TotalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
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

    @Autowired
    private ProvincialMeterMapper provincialMeterMapper;

    @Autowired
    private TotalService totalService;

    /**
     * 获取省计数据分析
     * @param billParam
     * @return
     */
    public Map<String,String> getProvinceCalculate(BillParam billParam) {

        if(billParam.getUserId()==null||"".equals(billParam.getUserId())){
            billParam.setUserId(totalService.getUserIdStr());
        }

        //创建反射判断条件
        String[] proStr= LevelConstants.PROSTR;

        //初始化数据
        Map<String,String> map= new HashMap<String, String>();
        for (int i = 0; i < proStr.length; i++) {
            map.put(proStr[i],0+"");
        }

        //根据时间和用户获取账单
        List<Total> one = totalService.listToal(billParam.getDate(),billParam.getUserId());

        //空值处理
        if(one==null || one.size()<=0){
            return map;
        }

        //获取相应的账单id
        String totalIdStr="";
        for(Total total:one){
            totalIdStr+=total.getTotalId()+",";
        }
        totalIdStr=totalIdStr.substring(0,totalIdStr.length()-1);

        List<ProvincialMeter> dailyTotalByTotalId = provincialMeterMapper.getDailyTotalByTotalId(totalIdStr);
        for(ProvincialMeter dailyTotal1:dailyTotalByTotalId){
            String[] split = dailyTotal1.getMeterText().split(",");
            for (int i = 0; i <proStr.length; i++) {
                Integer num=Integer.parseInt(map.get(proStr[i]))+Integer.parseInt(split[i]);
                map.put(proStr[i],num.toString());
            }
        }

        return map;
    }

    //    public Map<String,String> getProvinceCalculate(BillParam billParam) {
//
//        if(billParam.getUserId()==null||"".equals(billParam.getUserId())){
//            billParam.setUserId(totalService.getUserIdStr());
//        }
//
//        //创建反射的实体
//        ProvinceCalculate provinceCalculate;
//
//        //创建反射判断条件
//        String[] proStr={"北京","天津","河北","山西","内蒙古","辽宁","吉林","黑龙江","上海","江苏","浙江","安徽","福建",
//                "江西","山东","河南","湖北","湖南","广东","广西","海南","重庆","四川","贵州","云南","西藏","陕西","甘肃",
//                "青海","宁夏","新疆","台湾","香港","澳门"};
//
//        List<Total> one = totalService.listToal(billParam.getDate(),billParam.getUserId());
//        if(one.size()<=0){
//            provinceCalculate=new ProvinceCalculate();
//        }else{
//            String totalIdStr="";
//
//            for(Total total:one){
//                totalIdStr+=total.getTotalId()+",";
//            }
//            totalIdStr=totalIdStr.substring(0,totalIdStr.length()-1);
//
//            provinceCalculate = provinceCalculateMapper.getProvinceCalculate(totalIdStr);
//        }
//
//        Map<String,String> map= new HashMap<String, String>();
//        Field fields[]=provinceCalculate.getClass().getDeclaredFields();
//        String[] name=new String[fields.length];
//        Object[] value=new Object[fields.length];
//
//        try {
//            Field.setAccessible(fields, true);
//            for (int i = 3; i < value.length; i++) {
//                name[i] = fields[i].getName();
//                value[i] = fields[i].get(provinceCalculate);
//                map.put(proStr[i-3],value[i]==null?"0":value[i].toString());
////                System.out.println(name[i]+"-----------"+proStr[i-3]+"--------------"+value[i]);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return map;
//    }
}
