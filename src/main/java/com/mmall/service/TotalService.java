package com.mmall.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mmall.dto.BillDto;
import com.mmall.dto.ProfitsDto;
import com.mmall.excel.Bill;
import com.mmall.model.Response.Result;
import com.mmall.model.SysUserInfo;
import com.mmall.model.Total;
import com.mmall.model.params.BillDetailsParam;
import com.mmall.model.params.BillParam;
import com.mmall.model.params.TotalIncomeParam;
import com.mmall.model.params.TotalParam;
import com.mmall.vo.TotalVo;

import java.math.BigDecimal;
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

    IPage<TotalVo> getBill(IPage<TotalVo> page, BillDetailsParam billDetailsParam,Integer type);

    TotalIncomeParam getBillCount(String date);

    ProfitsDto getProfits(BillParam billParam);

   // String getPricing(Integer totalId);

    /**
     * 轮询
     * @param time
     * @param id
     * @return
     */
    Result polling(String time, Integer id,String fileName);


    Result<String> getPricing(Integer totalId,Integer type);

    String getUserIdStr();

    /**
     * 获取账单详情
     * @param userInfo
     * @param ipage
     * @return
     */
    Result getBillDetails(Integer status,SysUserInfo userInfo, String userId,String date, Page ipage);

    /**
     * 其他账单转发
     * @param billIds
     * @param userId
     * @return
     */
    Result othersBillForward(String billIds, Integer userId);

    Result deleteTotal(Integer totalId);

    List<SysUserInfo> getCollection();

    Result<List<Bill>> getBudget(Double weight, Integer userId);

    Result sendAll(TotalParam totalParam);
}
