package com.mmall.service.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mmall.dao.ProvinceCalculateMapper;
import com.mmall.model.ProvinceCalculate;
import com.mmall.service.ProvinceCalculateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 省计表 服务实现类
 * </p>
 *
 * @author qty
 * @since 2018-09-25
 */
@Service
public class ProvinceCalculateServiceImpl extends ServiceImpl<ProvinceCalculateMapper, ProvinceCalculate> implements ProvinceCalculateService {

    @Autowired
    private ProvinceCalculateMapper provinceCalculateMapper;

    /**
     * 获取省计数据分析
     * @param totalId
     * @return
     */
    public ProvinceCalculate getProvinceCalculate(String totalId) {
        return provinceCalculateMapper.getProvinceCalculate(totalId);
    }
}
