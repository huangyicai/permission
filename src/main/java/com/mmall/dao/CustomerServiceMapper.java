package com.mmall.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mmall.model.CustomerService;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hyc
 * @since 2018-10-09
 */
public interface CustomerServiceMapper extends BaseMapper<CustomerService> {
    /**
     * 获取快递公司所有工单
     * @param ipage
     * @param status
     * @param userId
     * @param waybillNumber
     * @param createTime
     * @param endTime
     * @return
     */
    Page<CustomerService> getAllCustomerServices(Page ipage,
                                                 @Param("status") Integer status,
                                                 @Param("userId")Integer userId,
                                                 @Param("waybillNumber")String waybillNumber,
                                                 @Param("createTime")String createTime,
                                                 @Param("endTime")String endTime);

    /**
     * 客户获取自己的工单
     * @param ipage
     * @param status
     * @param userId
     * @param waybillNumber
     * @return
     */
    Page<CustomerService> getAllCustomerServiceByUser(Page ipage,
                                     @Param("status") Integer status,
                                     @Param("userId")Integer userId,
                                     @Param("waybillNumber")String waybillNumber);

    /**
     * 客服获取自己处理的工单
     * @param ipage
     * @param status
     * @param id
     * @param waybillNumber
     */
    void getCustomerServiceBySelf(Page ipage, Integer status, Integer id, String waybillNumber);
}
