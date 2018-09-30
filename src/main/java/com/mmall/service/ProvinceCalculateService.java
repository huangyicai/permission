package com.mmall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mmall.model.ProvinceCalculate;

import java.util.Map;

/**
 * <p>
 * 省计表 服务类
 * </p>
 *
 * @author qty
 * @since 2018-09-25
 */
public interface ProvinceCalculateService extends IService<ProvinceCalculate> {
    Map<String,String> getProvinceCalculate(String totalId);
}
