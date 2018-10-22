package com.mmall.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mmall.dao.HandleTypeMapper;
import com.mmall.dao.WorkReplyMapper;
import com.mmall.model.CustomerService;
import com.mmall.dao.CustomerServiceMapper;
import com.mmall.model.Response.Result;
import com.mmall.model.SysUserInfo;
import com.mmall.model.WorkReply;
import com.mmall.model.params.CustomerServiceParam;
import com.mmall.service.CustomerServiceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hyc
 * @since 2018-10-09
 */
@Service
public class CustomerServiceServiceImpl extends ServiceImpl<CustomerServiceMapper, CustomerService> implements CustomerServiceService {
    @Autowired
    private HandleTypeMapper handleTypeMapper;
    @Autowired
    private CustomerServiceMapper customerServiceMapper;
    @Autowired
    private WorkReplyMapper workReplyMapper;
    @Override
    public Result saveCustomerService(SysUserInfo userInfo, CustomerServiceParam customerServiceParam) {
        String level = userInfo.getLevel().split("\\.")[2];
        CustomerService customerService = CustomerService.builder()
                .waybillNumber(customerServiceParam.getWaybillNumber())
                .contacts(customerServiceParam.getContacts())
                .content(customerServiceParam.getContent())
                .phone(customerServiceParam.getPhone())
                .enclosure(customerServiceParam.getEnclosure())
                .timeSlot(customerServiceParam.getTimeSlot())
                .typeId(customerServiceParam.getTypeId())
                .userId(userInfo.getId())
                .expressId(Integer.parseInt(level))
                .typeName(handleTypeMapper.selectById(customerServiceParam.getTypeId()).getTypeName())
                .build();
        customerServiceMapper.insert(customerService);
        return Result.ok(customerService);
    }

    @Override
    public Result getAllCustomerService(Integer status,Integer type, Integer expressId, Page ipage,String waybillNumber,String createTime,String endTime) {
        customerServiceMapper.getAllCustomerServices(ipage,status,type,expressId,waybillNumber, createTime,endTime);
        return Result.ok(ipage);
    }

    @Override
    public Result<Page<CustomerService>> getAllCustomerServiceByUser(Integer status, Integer userId, Page ipage, String waybillNumber) {
        customerServiceMapper.getAllCustomerServiceByUser(ipage,status,userId,waybillNumber);
        return Result.ok(ipage);
    }

    @Override
    public Result handleService(Integer handleId, SysUserInfo userInfo) {
        CustomerService customerService = customerServiceMapper.selectById(handleId);
        customerService.setHandleId(userInfo.getId());
        customerService.setHandleName(userInfo.getName());
        customerService.setStatus(2);
        customerServiceMapper.updateById(customerService);
        return Result.ok();
    }

    @Override
    public Result getCustomerServiceBySelf(Integer status,Integer type, Integer id, Page ipage, String waybillNumber) {
        customerServiceMapper.getCustomerServiceBySelf(ipage,status,type,id,waybillNumber);
        return Result.ok(ipage);
    }

    @Override
    public Result reply(Integer userId,Integer handleId, String content) {
        WorkReply workReply = WorkReply.builder()
                .content(content)
                .serviceId(handleId)
                .userId(userId)
                .build();
        workReplyMapper.insert(workReply);
        return Result.ok(workReply);
    }

    @Override
    public Result getReplys(Integer page,Integer size,Integer handleId) {
        List<WorkReply> workReplies = workReplyMapper.selectList(new QueryWrapper<WorkReply>()
                .eq("service_id", handleId)
                .eq("status", 0));
        for(WorkReply workReply:workReplies){
            workReply.setStatus(1);
            workReplyMapper.updateById(workReply);
        }
        IPage page1 = new Page(page,size);
        IPage iPage = workReplyMapper.selectPage(page1, new QueryWrapper<WorkReply>()
                .eq("service_id",handleId)
                .orderByDesc("create_time"));
        return Result.ok(iPage);
    }
}
