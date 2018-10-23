package com.mmall.service;

import com.mmall.dto.DailyTotalDto;
import com.mmall.model.DailyTotal;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mmall.model.Response.Result;
import com.mmall.model.params.BillParam;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 每日单量 服务类
 * </p>
 *
 * @author qty
 * @since 2018-10-19
 */
public interface DailyTotalService extends IService<DailyTotal> {
    List<DailyTotalDto> getDailyTotalList(BillParam billParam);
}
