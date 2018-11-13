package com.mmall.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mmall.Socket.ExpressWebSocket;
import com.mmall.dao.HandleTypeMapper;
import com.mmall.dao.SysUserInfoMapper;
import com.mmall.dao.WorkReplyMapper;
import com.mmall.dto.ReplyDto;
import com.mmall.dto.ReplynumServiceDto;
import com.mmall.model.CustomerService;
import com.mmall.dao.CustomerServiceMapper;
import com.mmall.model.Response.Result;
import com.mmall.model.SysUserInfo;
import com.mmall.model.WorkReply;
import com.mmall.model.params.CustomerServiceParam;
import com.mmall.service.CustomerServiceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mmall.util.DateTimeUtil;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @Autowired
    private SysUserInfoMapper sysUserInfoMapper;
    @Override
    public Result saveCustomerService(SysUserInfo userInfo, CustomerServiceParam customerServiceParam) {
        String level = userInfo.getLevel().split("\\.")[2];
        CustomerService customerService = CustomerService.builder()
                .waybillNumber(customerServiceParam.getWaybillNumber())
                .contacts(customerServiceParam.getContacts())
                .content(customerServiceParam.getContent())
                .phone(customerServiceParam.getPhone())
                .enclosure(customerServiceParam.getEnclosure())
                .receiveTime(customerServiceParam.getTimeSlot())
                .typeId(customerServiceParam.getTypeId())
                .userId(userInfo.getId())
                .expressId(Integer.parseInt(level))
                .typeName(handleTypeMapper.selectById(customerServiceParam.getTypeId()).getTypeName())
                .build();
        customerServiceMapper.insert(customerService);
        SysUserInfo sysUserInfo = sysUserInfoMapper.selectById(customerService.getExpressId());
        String content = "运单号:"+ customerService.getWaybillNumber();
        ExpressWebSocket.sendMsgAddServices(sysUserInfo,content,"",4);
        return Result.ok(customerService);
    }

    @Override
    public Result getAllCustomerService(Integer status,Integer type, Integer expressId, Page ipage,String waybillNumber,
                                        String createTime,String endTime,Integer receiveSolt,Integer endSolt) {
        Page<CustomerService> allCustomerServices = customerServiceMapper.getAllCustomerServices(ipage, status, type, expressId, waybillNumber, createTime, endTime,receiveSolt,endSolt);
        return Result.ok(ipage);
    }

    @Override
    public Result<Page<ReplynumServiceDto>> getAllCustomerServiceByUser(Integer status, Integer userId, Page ipage, String waybillNumber) {
        Page<ReplynumServiceDto> allCustomerServiceByUser = customerServiceMapper.getAllCustomerServiceByUser(ipage, status, userId, waybillNumber);
        for(ReplynumServiceDto cs : allCustomerServiceByUser.getRecords()){
            if(cs.getStatus()==2){
                Integer replyNum = workReplyMapper.selectCount(new QueryWrapper<WorkReply>()
                        .eq("service_id", cs.getId())
                        .eq("status", 0));
                cs.setReplyNum(replyNum);
            }
        }
        return Result.ok(allCustomerServiceByUser);
    }

    @Override
    public Result handleService(Integer handleId, SysUserInfo userInfo) {
        //当前时间
        long currentTime = System.currentTimeMillis();
        CustomerService customerService = customerServiceMapper.selectById(handleId);
        customerService.setHandleId(userInfo.getId());
        customerService.setReceiveTime(DateTimeUtil.numToDate(currentTime,"yyyy-MM-dd HH:mm:ss"));
        customerService.setHandleName(userInfo.getName());
        customerService.setStatus(2);
        //工单创建时间
        long createT = DateTimeUtil.DateToNum(customerService.getCreateTime());

        customerService.setReceiveTimeSolt((currentTime-createT)/(1000*60));
        customerServiceMapper.updateById(customerService);
        return Result.ok();
    }

    @Override
    public Result getCustomerServiceBySelf(Integer status,Integer type, Integer id, Page ipage, String waybillNumber) {
        Page<ReplynumServiceDto> customerServiceBySelf = customerServiceMapper.getCustomerServiceBySelf(ipage, status, type, id, waybillNumber);
        for(ReplynumServiceDto cs : customerServiceBySelf.getRecords()){
            if(cs.getStatus()==2){
                Integer replyNum = workReplyMapper.selectCount(new QueryWrapper<WorkReply>()
                        .eq("service_id", cs.getId())
                        .eq("service_type", 0));
                cs.setReplyNum(replyNum);
            }

        }
        return Result.ok(customerServiceBySelf);
    }

    @Override
    public Result reply(Integer userId,Integer handleId, String content) {
        WorkReply workReply = WorkReply.builder()
                .content(content)
                .serviceId(handleId)
                .userId(userId)
                .build();
        workReplyMapper.insert(workReply);
        CustomerService customerService = customerServiceMapper.selectById(handleId);
        SysUserInfo sysUserInfo = null;
        if(customerService.getHandleId()==userId||customerService.getHandleId().equals(userId)){
            sysUserInfo = sysUserInfoMapper.selectById(customerService.getUserId());
        }else {
            sysUserInfo = sysUserInfoMapper.selectById(customerService.getHandleId());
        }
        ExpressWebSocket.sendMsgAddServices(sysUserInfo,"回复："+content,"运单号:"+ customerService.getWaybillNumber(),5);
        return Result.ok(workReply);
    }

    @Override
    public Result getReplys(Integer page,Integer size,Integer handleId) {
        SysUserInfo user = (SysUserInfo) SecurityUtils.getSubject().getSession().getAttribute("user");
        if(user.getPlatformId()==3){
            List<WorkReply> workReplies = workReplyMapper.selectList(new QueryWrapper<WorkReply>()
                    .eq("service_id", handleId)
                    .eq("status", 0));
            for(WorkReply workReply:workReplies){
                workReply.setStatus(1);
                workReplyMapper.updateById(workReply);
            }
        }
        else{
            List<WorkReply> workReplies = workReplyMapper.selectList(new QueryWrapper<WorkReply>()
                    .eq("service_id", handleId)
                    .eq("service_type", 0));
            for(WorkReply workReply:workReplies){
                workReply.setServiceType(1);
                workReplyMapper.updateById(workReply);
            }
        }
        IPage page1 = new Page(page,size);
        IPage iPage = workReplyMapper.selectPage(page1, new QueryWrapper<WorkReply>()
                .eq("service_id",handleId)
                .orderByDesc("create_time"));
        return Result.ok(iPage);
    }

    @Override
    public Result getAllReplys(SysUserInfo user) {
        return Result.ok(puGetAllReplys(user,"express_id"));
    }

    public ReplyDto puGetAllReplys(SysUserInfo user,String column){
        ReplyDto rd = new ReplyDto();

        //处理完毕工单数
        Integer handledNum = customerServiceMapper.selectCount(new QueryWrapper<CustomerService>()
                .eq(column, user.getId()).eq("status",3));
        //处理中的工单数
        Integer handleingNum = customerServiceMapper.selectCount(new QueryWrapper<CustomerService>()
                .eq(column, user.getId()).eq("status",2));
        //未处理工单数
        Integer noHandleNum = customerServiceMapper.selectCount(new QueryWrapper<CustomerService>()
                .eq(column, user.getId()).eq("status",1));
        //总工单
        Integer totalallNum = noHandleNum + handleingNum +  handledNum;
        rd.setTotalallNum(totalallNum);
        rd.setHandledNum(handledNum);
        rd.setHandleingNum(handleingNum);
        rd.setNoHandleNum(noHandleNum);
        rd.setName(user.getName());
        rd.setUser_id(user.getId());
        rd.setDisplay(user.getDisplay());
        rd.setPersonInCharge(user.getPersonInCharge());
        return rd;
    }

    @Override
    public Result getAllReplysByService(SysUserInfo user) {
        List<ReplyDto> lr = Lists.newArrayList();
        List<SysUserInfo> sysUserInfos = sysUserInfoMapper.selectList(new QueryWrapper<SysUserInfo>()
                .notIn("status", -1)
                .eq("platform_id", 2)
                .eq("parent_id", user.getId()));
        sysUserInfos.add(user);
        for(SysUserInfo Info : sysUserInfos){
            lr.add(puGetAllReplys(Info,"handle_id"));
        }
        return Result.ok(lr);
    }

    @Override
    public Result getAllByNoHandle(SysUserInfo user) {
        //未处理工单数
        Integer noHandleNum = customerServiceMapper.selectCount(new QueryWrapper<CustomerService>()
                .eq("express_id", user.getId()).eq("status",1));
        return Result.ok(noHandleNum);
    }
}
