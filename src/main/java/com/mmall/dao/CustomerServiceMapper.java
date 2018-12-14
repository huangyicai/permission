package com.mmall.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mmall.dto.ReplynumServiceDto;
import com.mmall.model.CustomerService;
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
                                                 @Param("type") Integer type,
                                                 @Param("userId")Integer userId,
                                                 @Param("waybillNumber")String waybillNumber,
                                                 @Param("keyName")String keyName,
                                                 @Param("createTime")String createTime,
                                                 @Param("endTime")String endTime,
                                                 @Param("receiveSolt")Integer receiveSolt,
                                                 @Param("endSolt")Integer endSolt);

    /**
     * 获取快递公司所有工单--导出
     * @param status
     * @param userId
     * @param waybillNumber
     * @param createTime
     * @param endTime
     * @return
     */
    List<CustomerService> getAllCustomerServices(@Param("status") Integer status,
                                                 @Param("type") Integer type,
                                                 @Param("userId")Integer userId,
                                                 @Param("waybillNumber")String waybillNumber,
                                                 @Param("keyName")String keyName,
                                                 @Param("createTime")String createTime,
                                                 @Param("endTime")String endTime,
                                                 @Param("receiveSolt")Integer receiveSolt,
                                                 @Param("endSolt")Integer endSolt);

    /**
     * 客户获取自己的工单
     * @param ipage
     * @param status
     * @param userId
     * @param waybillNumber
     * @return
     */
    Page<ReplynumServiceDto> getAllCustomerServiceByUser(Page ipage,
                                                         @Param("errorId") Integer errorId,
                                                         @Param("status") Integer status,
                                                        @Param("userId")Integer userId,
                                                        @Param("waybillNumber")String waybillNumber);

    /**
     * 客服获取自己处理的工单
     * @param ipage
     * @param status
     * @param waybillNumber
     */
    Page<ReplynumServiceDto> getCustomerServiceBySelf(Page ipage,
                                                      @Param("status") Integer status,
                                                      @Param("type") Integer type,
                                                      @Param("userId")Integer userId,
                                                      @Param("waybillNumber")String waybillNumber,
                                                      @Param("createTime")String createTime,
                                                      @Param("endTime")String endTime);

    int getCountServiceByHandleId(@Param("type")Integer type,
                                  @Param("userId")Integer userId,
                                  @Param("status")Integer status,
                                  @Param("dateBegin")String dateBegin,
                                  @Param("dateEnd")String dateEnd);

    int updateCustomerService(@Param("userId")Integer userId,
                               @Param("handleId")String handleId,
                               @Param("name")String name);
}
