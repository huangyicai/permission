package com.mmall.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mmall.dto.BillDto;
import com.mmall.dto.ProfitsDto;
import com.mmall.model.Response.Result;
import com.mmall.model.Total;
import com.mmall.model.params.BillDetailsParam;
import com.mmall.model.params.BillParam;
import com.mmall.model.params.TotalIncomeParam;
import org.springframework.web.multipart.MultipartFile;

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

    Page<Total> getBill(Page<Total> page, BillDetailsParam billDetailsParam);

    TotalIncomeParam getBillCount(String date);

    ProfitsDto getProfits(BillParam billParam);

<<<<<<< HEAD
    String getPricing(Integer totalId);

    /**
     * 轮询
     * @param time
     * @param id
     * @return
     */
    Result polling(String time, Integer id);
=======
    Result<String> getPricing(Integer totalId);

    String getUserIdStr();
>>>>>>> b724a2df59160b068f29f2a73d45034887562a5d
}
