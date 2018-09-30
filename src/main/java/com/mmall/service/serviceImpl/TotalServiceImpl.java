package com.mmall.service.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mmall.dao.TotalMapper;
import com.mmall.model.Total;
import com.mmall.model.params.TotalIncomeParam;
import com.mmall.service.TotalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    /**
     * 获取客户月计统计
     * @param totalTime
     * @param userId
     * @return
     */
    public Total getToal(String totalTime, String userId) {
        return totalMapper.getToal(totalTime,userId);
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
     * @param totalTime
     * @param userId
     * @param state
     * @return
     */
    public List<Total> getBill(String totalTime, String userId, Integer state) {
        return totalMapper.getBill(totalTime,userId,state);
    }

    /**
     * 获取已收和未收金额
     * @param totalTime
     * @param userId
     * @return
     */
    public TotalIncomeParam getBillCount(String totalTime, String userId) {
        String staStr="1,2,3";
        String steStr="4";
        Total totalPaid = totalMapper.getBillCount(totalTime, userId, steStr);
        Total totalOffer = totalMapper.getBillCount(totalTime, userId, staStr);

        TotalIncomeParam totalIncomeParam=new TotalIncomeParam();
        if(totalPaid!=null && totalPaid.getTotalPaid()!=null){
            totalIncomeParam.setTotalPaid(totalPaid.getTotalPaid());
        }
        if(totalOffer!=null && totalOffer.getTotalOffer()!=null){
            totalIncomeParam.setTotalOffer(totalOffer.getTotalOffer());
        }
        return totalIncomeParam;
    }
}
