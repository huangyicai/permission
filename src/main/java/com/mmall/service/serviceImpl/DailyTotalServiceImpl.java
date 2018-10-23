package com.mmall.service.serviceImpl;

import com.mmall.dto.DailyTotalDto;
import com.mmall.model.DailyTotal;
import com.mmall.dao.DailyTotalMapper;
import com.mmall.model.Total;
import com.mmall.model.WeightCalculate;
import com.mmall.model.params.BillParam;
import com.mmall.service.DailyTotalService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mmall.service.TotalService;
import com.mmall.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 每日单量 服务实现类
 * </p>
 *
 * @author qty
 * @since 2018-10-19
 */
@Service
public class DailyTotalServiceImpl extends ServiceImpl<DailyTotalMapper, DailyTotal> implements DailyTotalService {

    @Autowired
    private DailyTotalMapper dailyTotalMapper;

    @Autowired
    private TotalService totalService;

    @Override
    public List<DailyTotalDto> getDailyTotalList(BillParam billParam) {

        //获取相应用户的每日单量
        if(billParam.getUserId()==null||"".equals(billParam.getUserId())){
            billParam.setUserId(totalService.getUserIdStr());
        }

        //获取当月账单
        List<Total> list = totalService.listToal(billParam.getDate(),billParam.getUserId());

        //初始化数据
        List<DailyTotalDto> dailyTotalDtos=new ArrayList<>();
        int days = DateUtils.getDays(billParam.getDate());
        for (int i = 1; i <=days; i++) {
            DailyTotalDto dd=new DailyTotalDto();
            dd.setDay(i+"日");
            dailyTotalDtos.add(dd);
        }

        String totalIdStr="";
        for(Total total:list){
            totalIdStr+=total.getTotalId()+",";
        }

        //空值处理
        if(list==null || list.size()<=0){
            return dailyTotalDtos;
        }
        totalIdStr=totalIdStr.substring(0,totalIdStr.length()-1);

        //获取公司某一月的每日賬單
        List<DailyTotal> dailyTotalListByTotalId = dailyTotalMapper.getDailyTotalListByTotalId(totalIdStr, billParam.getDate());

        for(DailyTotal dailyTotal1:dailyTotalListByTotalId){
            String[] split = dailyTotal1.getDailyText().split(",");
            for (int i = 0; i <split.length; i++) {
                for (DailyTotalDto dd:dailyTotalDtos){
                    if((i+1+"日").equals(dd.getDay())){
                        dd.setNum(dd.getNum()+Integer.parseInt(split[i]));
                    }
                }
            }
        }

        return dailyTotalDtos;
    }
}
