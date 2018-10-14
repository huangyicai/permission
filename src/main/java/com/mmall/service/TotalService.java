package com.mmall.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mmall.dto.BillDto;
import com.mmall.dto.ProfitsDto;
import com.mmall.model.Response.Result;
import com.mmall.model.Total;
import com.mmall.model.params.BillDetailsParam;
import com.mmall.model.params.BillParam;
import com.mmall.model.params.TotalIncomeParam;
import com.mmall.vo.TotalVo;

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

    IPage<TotalVo> getBill(IPage<TotalVo> page, BillDetailsParam billDetailsParam);

    TotalIncomeParam getBillCount(String date);

    ProfitsDto getProfits(BillParam billParam);

   // String getPricing(Integer totalId);

    /**
     * 轮询
     * @param time
     * @param id
     * @return
     */
    Result polling(String time, Integer id);
    Result<String> getPricing(Integer totalId);

    String getUserIdStr();
}
