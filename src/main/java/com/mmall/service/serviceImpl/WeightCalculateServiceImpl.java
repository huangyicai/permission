package com.mmall.service.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mmall.dao.WeightCalculateMapper;
import com.mmall.model.WeightCalculate;
import com.mmall.service.WeightCalculateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 重量区间计算表 服务实现类
 * </p>
 *
 * @author qty
 * @since 2018-09-25
 */
@Service
public class WeightCalculateServiceImpl extends ServiceImpl<WeightCalculateMapper, WeightCalculate> implements WeightCalculateService {

    @Autowired
    private WeightCalculateMapper weightCalculateMapper;

    /**
     * 获取重量区间的统计
     * @param totalId
     * @return
     */
    public WeightCalculate getWeightCalculate(String totalId) {
        return weightCalculateMapper.getWeightCalculate(totalId);
    }
}
