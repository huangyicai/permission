package com.mmall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mmall.model.Total;

/**
 * <p>
 * 月计表(客户账单) 服务类
 * </p>
 *
 * @author qty
 * @since 2018-09-25
 */
public interface TotalService extends IService<Total> {
    Total getToal(String totalTime,String userId);
}
