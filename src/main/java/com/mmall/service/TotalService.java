package com.mmall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mmall.model.Total;
import com.mmall.model.params.TotalIncomeParam;

import java.util.List;

/**
 * <p>
 * 月计表(客户账单) 服务类
 * </p>
 *
 * @author qty
 * @since 2018-09-25
 */
public interface TotalService extends IService<Total> {
    Total getToal(String totalTime,String userId);

    List<Total> listToal(String totalTime,String userId);

    List<Total> getBill(String totalTime,String userId,Integer state);

    TotalIncomeParam getBillCount(String totalTime, String userId);
}
