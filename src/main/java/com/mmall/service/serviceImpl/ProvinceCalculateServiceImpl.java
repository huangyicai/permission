package com.mmall.service.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mmall.constants.LevelConstants;
import com.mmall.dao.ProvinceCalculateMapper;
import com.mmall.dao.ProvincialMeterMapper;
import com.mmall.dto.DailyTotalDto;
import com.mmall.dto.ProvinceCalculateDto;
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
import java.util.ArrayList;
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

    /**
     * 获取省计数据分析---app
     * @param billParam
     * @return
     */
    @Override
    public List<ProvinceCalculateDto> getProvinceCalculateDto(BillParam billParam) {

        if(billParam.getUserId()==null||"".equals(billParam.getUserId())){
            billParam.setUserId(totalService.getUserIdStr());
        }

        //创建反射判断条件
        String[] proStr= LevelConstants.PROSTR;

        //初始化数据
        List<ProvinceCalculateDto> list=new ArrayList<>();
        for (int i = 0; i < proStr.length; i++) {
            ProvinceCalculateDto pd=new ProvinceCalculateDto();
            pd.setCity(proStr[i]);
            pd.setNum(0);
            list.add(pd);
        }

        //根据时间和用户获取账单
        List<Total> one = totalService.listToal(billParam.getDate(),billParam.getUserId());

        //空值处理
        if(one==null || one.size()<=0){
            return list;
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
                for(ProvinceCalculateDto provinceCalculateDto:list){
                    if(proStr[i].equals(provinceCalculateDto.getCity())){
                        provinceCalculateDto.setNum(provinceCalculateDto.getNum()+Integer.parseInt(split[i]));
                        break;
                    }
                }
            }
        }
        return list;
    }
}
