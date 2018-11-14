package com.mmall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mmall.dto.ProvinceCalculateDto;
import com.mmall.model.ProvinceCalculate;
import com.mmall.model.params.BillParam;

import java.util.List;
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

    Map<String,String> getProvinceCalculate(BillParam billParam);

    List<ProvinceCalculateDto> getProvinceCalculateDto(BillParam billParam);
}
