package com.mmall.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mmall.Socket.ExpressWebSocket;
import com.mmall.config.UserInfoConfig;
import com.mmall.constants.LevelConstants;
import com.mmall.dao.*;
import com.mmall.dto.ReplyDto;
import com.mmall.dto.ReplynumServiceDto;
import com.mmall.model.BillKeyword;
import com.mmall.model.CustomerService;
import com.mmall.model.Response.InfoEnums;
import com.mmall.model.Response.Result;
import com.mmall.model.SysUserInfo;
import com.mmall.model.WorkReply;
import com.mmall.model.params.CustomerServiceParam;
import com.mmall.service.CustomerServiceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mmall.service.ExpressUserService;
import com.mmall.util.DateTimeUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
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
    @Autowired
    private BillKeywordMapper billKeywordMapper;
    @Override
    public Result saveCustomerService(SysUserInfo userInfo, CustomerServiceParam customerServiceParam) {
        String level = userInfo.getLevel().split("\\.")[2];
        CustomerService customerService = CustomerService.builder()
                .waybillNumber(customerServiceParam.getWaybillNumber())
                .userKey(customerServiceParam.getUserKey())
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

        List<SysUserInfo> sysUserInfos = sysUserInfoMapper.selectList(new QueryWrapper<SysUserInfo>()
                .eq("parent_id", sysUserInfo.getId())
                .eq("platform_id", LevelConstants.EXPRESS)
                .in("status",1,0));
        sysUserInfos.add(sysUserInfo);
        ExpressWebSocket.sendMsgAddServicesLists(sysUserInfos,content,"",4);
        return Result.ok(customerService);
    }

    @Override
    public Result getAllCustomerService(Integer status,Integer type, Integer expressId, Page ipage,String waybillNumber,
                                        String keyName,
                                        String createTime,String endTime,Integer receiveSolt,Integer endSolt) {
        Page<CustomerService> allCustomerServices = customerServiceMapper.getAllCustomerServices(ipage, status, type, expressId, waybillNumber, keyName, createTime, endTime,receiveSolt,endSolt);
        return Result.ok(ipage);
    }

    @Override
    public Result<Page<ReplynumServiceDto>> getAllCustomerServiceByUser(Integer errorId,Integer status, Integer userId, Page ipage, String waybillNumber) {
        Page<ReplynumServiceDto> allCustomerServiceByUser = customerServiceMapper.getAllCustomerServiceByUser(ipage, errorId,status, userId, waybillNumber);
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

        if(customerService.getStatus()==2){
            return Result.error(InfoEnums.SERVICE_HANDLED);
        }
        customerService.setHandleId(userInfo.getId());
        customerService.setReceiveTime(DateTimeUtil.numToDate(currentTime,"yyyy-MM-dd HH:mm:ss"));
        customerService.setHandleName(userInfo.getName());
        customerService.setStatus(2);
        //工单创建时间
        long createT = DateTimeUtil.DateToNum(customerService.getCreateTime());

        customerService.setReceiveTimeSolt((currentTime-createT)/(1000*60));
        customerServiceMapper.updateById(customerService);
        SysUserInfo userInfoExpress = UserInfoConfig.getUserInfo();
        List<SysUserInfo> sysUserInfos = sysUserInfoMapper.selectList(new QueryWrapper<SysUserInfo>()
                .eq("parent_id", userInfoExpress.getId())
                .eq("platform_id", LevelConstants.EXPRESS)
                .in("status",1,0));
        sysUserInfos.add(userInfoExpress);
        sysUserInfos.remove(userInfo);
        ExpressWebSocket.sendMsgAddServicesLists(sysUserInfos,"","",6);
        return Result.ok();
    }

    @Override
    public Result getCustomerServiceBySelf(Integer status,Integer type, Integer id, Page ipage, String waybillNumber,String createTime,String endTime) {
        Page<ReplynumServiceDto> customerServiceBySelf = customerServiceMapper.getCustomerServiceBySelf(ipage, status, type, id, waybillNumber,createTime,endTime);
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
    public Result getAllReplys(SysUserInfo user,String dateBegin,String dateEnd) {
        return Result.ok(puGetAllReplys(user,2,dateBegin,dateEnd));
    }

    public ReplyDto puGetAllReplys(SysUserInfo user,Integer column,String dateBegin,String dateEnd){
        ReplyDto rd = new ReplyDto();

        //处理完毕工单数
        Integer handledNum = customerServiceMapper.getCountServiceByHandleId(column,user.getId(),3,dateBegin,dateEnd);
        //处理中的工单数
        Integer handleingNum = customerServiceMapper.getCountServiceByHandleId(column,user.getId(),2,dateBegin,dateEnd);
        //未处理工单数
        Integer noHandleNum = customerServiceMapper.getCountServiceByHandleId(column,user.getId(),1,dateBegin,dateEnd);
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
    public Result getAllReplysByService(SysUserInfo user,String dateBegin,String dateEnd) {
        List<ReplyDto> lr = Lists.newArrayList();
        List<SysUserInfo> sysUserInfos = sysUserInfoMapper.selectList(new QueryWrapper<SysUserInfo>()
                .notIn("status", -1)
                .eq("platform_id", 2)
                .eq("parent_id", user.getId()));
        sysUserInfos.add(user);
        for(SysUserInfo Info : sysUserInfos){
            lr.add(puGetAllReplys(Info,1,dateBegin,dateEnd));
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

    @Override
    public Result getUserKeys(SysUserInfo userInfo) {
        List<BillKeyword> userKeys = billKeywordMapper.selectList(new QueryWrapper<BillKeyword>()
                .eq("user_id", userInfo.getId()).eq("status",1));
        return Result.ok(userKeys);
    }

    @Override
    public void createExcel(HttpServletResponse response) {
        //创建workbook
        HSSFWorkbook workbook = new HSSFWorkbook();
        //添加Worksheet（不添加sheet时生成的xls文件打开时会报错)
        Sheet sheet1 = workbook.createSheet("sheet1");
        OutputStream out = null;
        try {
            out = response.getOutputStream();
            String fileName = "test.xls";// 文件名
            response.setContentType("application/x-msdownload");
            response.setHeader("Content-Disposition", "attachment; filename="
                    + URLEncoder.encode(fileName, "UTF-8"));
            Row row = workbook.getSheet("sheet1").createRow(0);    //创建第一行
            for(int i = 0;i < 10;i++){
                Cell cell = row.createCell(i);
                cell.setCellValue(i);
            }
            workbook.write(out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
