package com.mmall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mmall.model.WeightCalculate;
import com.mmall.model.params.BillParam;

import java.util.Map;

/**
 * <p>
 * 重量区间计算表 服务类
 * </p>
 *
 * @author qty
 * @since 2018-09-25
 */
public interface WeightCalculateService extends IService<WeightCalculate> {
    Map<String,String> getWeightCalculate(BillParam billParam);
}
