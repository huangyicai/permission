package com.mmall.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mmall.dto.BillDto;
import com.mmall.dto.ProfitsDto;
import com.mmall.model.Total;
import com.mmall.model.params.BillDetailsParam;
import com.mmall.model.params.BillParam;
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
    BillDto getBillData(BillParam billParam);

    List<Total> listToal(String totalTime,String userId);

    List<Total> getBill(Page<Total> page, BillDetailsParam billDetailsParam);

    TotalIncomeParam getBillCount(BillDetailsParam billDetailsParam);

    ProfitsDto getProfits(BillParam billParam);
}
