package com.mmall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mmall.model.ProvinceCalculate;

/**
 * <p>
 * 省计表 服务类
 * </p>
 *
 * @author qty
 * @since 2018-09-25
 */
public interface ProvinceCalculateService extends IService<ProvinceCalculate> {
    ProvinceCalculate getProvinceCalculate(String totalId);
}
