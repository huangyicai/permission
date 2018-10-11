package com.mmall.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mmall.model.CustomerService;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mmall.model.Response.Result;
import com.mmall.model.SysUserInfo;
import com.mmall.model.params.CustomerServiceParam;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hyc
 * @since 2018-10-09
 */
public interface CustomerServiceService extends IService<CustomerService> {
    /**
     * 添加客服工单
     * @param customerServiceParam
     * @return
     */
    Result saveCustomerService(SysUserInfo userInfo, CustomerServiceParam customerServiceParam);

    /**
     * 获取快递公司所有工单
     * @param status
     * @return
     */
    Result getAllCustomerService(Integer status, Integer userId, Page ipage,String waybillNumber,String createTime,String endTime);

    /**
     * 获取客户自己的工单
     * @param status
     * @param id
     * @param ipage
     * @param waybillNumber
     * @return
     */
    Result getAllCustomerServiceByUser(Integer status, Integer id, Page ipage, String waybillNumber);

    /**
     * 客服接取工单
     * @param handleId
     * @return
     */
    Result handleService(Integer handleId, SysUserInfo userInfo);

    /**
     * 客服获取自己处理的工单
     * @param status
     * @param id
     * @param ipage
     * @param waybillNumber
     * @return
     */
    Result getCustomerServiceBySelf(Integer status, Integer id, Page ipage, String waybillNumber);
}
