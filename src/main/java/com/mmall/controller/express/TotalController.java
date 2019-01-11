package com.mmall.controller.express;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mmall.Socket.ExpressWebSocket;
import com.mmall.config.UserInfoConfig;
import com.mmall.dao.SumTatalMapper;
import com.mmall.dao.SysUserInfoMapper;
import com.mmall.dao.TotalMapper;
import com.mmall.dto.BillDto;
import com.mmall.dto.ProfitsDto;
import com.mmall.excel.Bill;
import com.mmall.model.Response.InfoEnums;
import com.mmall.model.SumTatal;
import com.mmall.model.SysUserInfo;
import com.mmall.model.params.*;
import com.mmall.excel.imp.XlsxProcessAbstract;
import com.mmall.model.Response.Result;
import com.mmall.model.Total;
import com.mmall.service.TotalService;
import com.mmall.util.DateHelp;
import com.mmall.vo.TotalVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 重量件数月计表 前端控制器
 * </p>
 *
 * @author qty
 * @since 2018-09-25
 */
@Api(value = "TotalController", description = "账单管理")
@RestController
@RequestMapping("/express/total")
public class TotalController {

    @Autowired
    private TotalService totalService;

    @Autowired
    private TotalMapper totalMapper;

    @Autowired
    private SumTatalMapper sumTatalMapper;

    @Autowired
    private SysUserInfoMapper sysUserInfoMapper;

    @ApiOperation(value = "确认收款",  notes="需要Authorization")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "type",value = "类型：1-全部付款，2-部分付款",dataType = "Integer",paramType = "path")
    )
    @GetMapping(value = "/update/{totalId}/{type}")
    public Result updateSatte(@PathVariable(value = "totalId") Integer totalId,
                              @PathVariable(value = "type") Integer type,
                              @RequestParam(value = "money") String money){

        Total byId = totalService.getById(totalId);

        if(byId.getTotalState()==2||byId.getTotalState()==5||byId.getTotalState()==3){
            Integer state=(type==1?4:5);
            Total total=new Total();
            total.setTotalId(totalId);
            total.setTotalPaid(byId.getTotalPaid().add(new BigDecimal(money)));
            total.setTotalState(state);
            total.setUpdateTime(new Date());
            totalService.updateById(total);
        }else{
            return Result.error(InfoEnums.NO_PAYMENT);
        }

        return Result.ok();
    }

    @ApiOperation(value = "自我结账",  notes="需要Authorization")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "totalId",value = "账单id",dataType = "long",paramType = "path"),
            @ApiImplicitParam(name = "type",value = "类型：1-全部付款，2-部分付款",dataType = "long",paramType = "path"),
            @ApiImplicitParam(name = "money",value = "收款金额",dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "totalCredentialsUrl",value = "凭证路径",dataType = "String",paramType = "query")
    } )
    @GetMapping(value = "/selfCollection/{totalId}/{type}")
    public Result selfCollection(@PathVariable(value = "totalId") Integer totalId,
                              @PathVariable(value = "type") Integer type,
                              @RequestParam(value = "money") String money,
                              @RequestParam(value = "totalCredentialsUrl") String totalCredentialsUrl){

        Total byId = totalService.getById(totalId);
        if(byId.getTotalState()==2||byId.getTotalState()==5||byId.getTotalState()==3){
            String url="".equals(byId.getTotalCredentialsUrl())?totalCredentialsUrl:byId.getTotalCredentialsUrl()+"$$$"+totalCredentialsUrl;
            Integer state=(type==1?4:5);
            Total total=new Total();
            total.setTotalId(totalId);
            total.setTotalPaid(byId.getTotalPaid().add(new BigDecimal(money)));
            total.setTotalState(state);
            total.setUpdateTime(new Date());
            total.setTotalCredentialsUrl(url);
            totalService.updateById(total);
        }else{
            return Result.error(InfoEnums.NO_PAYMENT);
        }
        return Result.ok();
    }

    @ApiOperation(value = "确认收款提示",  notes="需要Authorization")
    @GetMapping(value = "/getCollection")
    public Result<List<SysUserInfo>> getCollection(){
        List<SysUserInfo> collection = totalService.getCollection();
        return Result.ok(collection);
    }


    @ApiOperation(value = "删除订单",  notes="需要Authorization")
    @DeleteMapping(value = "/deleteTotal/{totalId}")
    public Result deleteTotal(@PathVariable(value = "totalId") String totalId){
        return totalService.deleteTotal(totalId);
    }

    @ApiOperation(value = "修改账单(暂时不适用)",  notes="需要Authorization")
    @GetMapping(value = "/updateMoney")
    public Result updateMoney(TotalParam totalParam){
        Total total=new Total();
        total.setTotalId(Integer.parseInt(totalParam.getTotalId()));
        totalService.updateById(total);
        return Result.ok();
    }

    @ApiOperation(value = "发送账单",notes = "需要Authorization")
    @PostMapping(value = "/send",produces = {"application/json;charest=Utf-8"})
    public Result send(@RequestBody TotalParam totalParam){
        Total byId = totalService.getById(totalParam.getTotalId());

        if(byId.getTotalState()==1){
            totalMapper.updateByTotalId(totalParam.getTotalId(),totalParam.getTotalRemark(),totalParam.getDate(),new BigDecimal(totalParam.getTotalAdditional()));
            SysUserInfo sysUserInfo = sysUserInfoMapper.selectById(byId.getUserId());
            ExpressWebSocket.sendMsg(sysUserInfo,byId,1);
            return Result.ok();
        }else {
            return Result.error(InfoEnums.SEND_FAILURE);
        }
    }

    @ApiOperation(value = "批量发送账单",notes = "需要Authorization")
    @PutMapping(value = "/sendAll",produces = {"application/json;charest=Utf-8"})
    public Result sendAll(@RequestBody TotalParam totalParam){
        if("".equals(totalParam.getTotalId()) || totalParam.getTotalId()==null){
            return Result.error(InfoEnums.BILL_IS_NULL);
        }
        return totalService.sendAll(totalParam);
    }

    @ApiOperation(value = "上传账单---追加",  notes="需要Authorization")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "time",value = "时间",dataType = "String",paramType = "path")
    )
    @PostMapping(value = "/additional/{time}",produces = {"application/json;charest=Utf-8"})
    public Result additional(MultipartFile file,@PathVariable("time")String time) throws Exception {
        XlsxProcessAbstract xlsxProcessAbstract=new XlsxProcessAbstract();
        return xlsxProcessAbstract.processAllSheet(file,time,1,null);
    }

    @ApiOperation(value = "上传账单---替换",  notes="需要Authorization")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "time", value = "时间", dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "sumId", value = "总账单id", dataType = "String", paramType = "path")
    })
    @PostMapping(value = "/replace/{time}/{sumId}")
    public Result replace(MultipartFile file,
                          @PathVariable("time")String time,
                          @PathVariable("sumId") String sumId) throws Exception {
        XlsxProcessAbstract xlsxProcessAbstract=new XlsxProcessAbstract();
        return xlsxProcessAbstract.processAllSheet(file,time,2,sumId);
    }

    @ApiOperation(value = "替换账单--检测是否有已经发送的订单",  notes="需要Authorization")
    @PostMapping(value = "/retrieve/{sumId}")
    public Result retrieve(@RequestBody TotalIsTime totalIsTime,
                          @PathVariable("sumId") String sumId){
        List<Total> totals = totalMapper.listTotal(totalIsTime.getTime(), sumId,2);
        return Result.ok(totals);
    }

    @ApiOperation(value = "客户追加上传",  notes="需要Authorization")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId",value = "用户id",dataType = "Integer",paramType = "path"),
            @ApiImplicitParam(name = "time",value = "时间",dataType = "String",paramType = "path"),
    })
    @PostMapping(value = "/additionalSet/{userId}/{time}")
    public Result additionalSet(MultipartFile file,
                                @PathVariable("time") String time,
                                @PathVariable("userId") Integer userId) throws Exception {
        XlsxProcessAbstract xlsxProcessAbstract=new XlsxProcessAbstract();
        return xlsxProcessAbstract.additionalSet(file,userId,time);
    }

    @ApiOperation(value = "获取该月总账单",  notes="需要Authorization")
    @PostMapping(value = "/judgeSet/{time}")
    public Result<List<SumTatal>> judgeSet(@PathVariable("time") String time){
        SysUserInfo userInfo = UserInfoConfig.getUserInfo();
        List<SumTatal> sumTatals = sumTatalMapper.selectList(new QueryWrapper<SumTatal>()
                .eq("sum_time", time)
                .eq("user_id",userInfo.getId()));
       return Result.ok(sumTatals);
    }

    @ApiOperation(value = "获取账单月份总计",  notes="需要Authorization")
    @PostMapping(value = "/getBillData")
    public Result<BillDto> getBillData(@RequestBody BillParam billParam){
        BillDto billData = totalService.getBillData(billParam);
        return Result.ok(billData);
    }

    @ApiOperation(value = "获取账单列表",  notes="需要Authorization")
    @PostMapping(value = "/getBill")
    public Result<IPage<TotalVo>> getBill(@RequestBody BillDetailsParam billDetailsParam){
        IPage<TotalVo> page=new Page<>(billDetailsParam.getCurrent(),billDetailsParam.getSize());
        IPage<TotalVo> bill = totalService.getBill(page,billDetailsParam,0);
        return Result.ok(bill);
    }

    @ApiOperation(value = "获取账单详情：已收入和未收入",  notes="需要Authorization")
    @PostMapping(value = "/getIncome/{date}")
    public Result<TotalIncomeParam> getIncome(@PathVariable("date") String date){
        TotalIncomeParam billCount = totalService.getBillCount(date);
        return Result.ok(billCount);
    }

    @ApiOperation(value = "利润分析",  notes="需要Authorization")
    @PostMapping(value = "/getProfits")
    public Result<ProfitsDto> getProfits(@RequestBody BillParam billParam){
        ProfitsDto profits = totalService.getProfits(billParam);
        return Result.ok(profits);
    }

    @ApiOperation(value = "定价",  notes="需要Authorization")

    @GetMapping(value = "/getPricing/{totalId}")
    public Result getPricing(@PathVariable("totalId")Integer totalId){
        return  totalService.getPricing(totalId);
    }

    @ApiOperation(value = "试算",  notes="需要Authorization")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "weight",value = "重量",dataType = "Double",paramType = "query"),
            @ApiImplicitParam(name = "userId",value = "客户id",dataType = "Integer",paramType = "query")
    })
    @GetMapping(value = "/getBudget")
    public Result<List<Bill>> getBudget(@RequestParam Double weight, @RequestParam Integer userId){
        Result<List<Bill>> budget = totalService.getBudget(weight, userId);
        return budget;
    }

    @ApiOperation(value = "重新上传",  notes="需要Authorization")
    @PostMapping(value = "/againSet/{totalId}")
    public Result againSet(@PathVariable("totalId")Integer totalId,
                                   MultipartFile file) throws Exception {
        XlsxProcessAbstract xlsxProcessAbstract=new XlsxProcessAbstract();
        return xlsxProcessAbstract.againSet(file,totalId);
    }

    @ApiOperation(value = "轮询账单表",  notes="需要Authorization")
    @GetMapping(produces = {"application/json;charest=Utf-8"})
    public Result polling(@RequestParam("time") String time,
                          @RequestParam("fileName") String fileName){
        SysUserInfo user = UserInfoConfig.getUserInfo();
        return totalService.polling(time,user.getId(),fileName);
    }

    @ApiOperation(value = "获取账单详情",  notes="需要Authorization")
    @GetMapping(value = "/list/{status}",produces = {"application/json;charest=Utf-8"})
    public Result getBillDetails(@RequestParam(name = "userId",required = false) String userId,
                                 @RequestParam(name = "date") String date,
                                 @RequestParam(name = "endDate" , required = false , defaultValue = "") String endDate,
                                 @PathVariable("status")Integer status,
                                 @RequestParam(name = "page",required = false,defaultValue = "1")Integer page,
                                 @RequestParam(name = "size",required = false,defaultValue = "10")Integer size){

        SysUserInfo userInfo = UserInfoConfig.getUserInfo();
        Page ipage = new Page(page,size);
        return totalService.getBillDetails( status,userInfo,userId,date,endDate,ipage);
    }

    @ApiOperation(value = "其他账单转发",  notes="需要Authorization")
    @GetMapping(value = "/bill/forward/{userId}",produces = {"application/json;charest=Utf-8"})
    public Result othersBillForward(@PathVariable("userId") Integer userId,
                                    @RequestParam("billIds") String billIds){
        return totalService.othersBillForward(billIds,userId);
    }

    @ApiOperation(value = "一键定价",  notes="需要Authorization")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "totalId",value = "账单id",dataType = "string",paramType = "query")
    )
    @GetMapping(value = "/keyPricing")
    public Result keyPricing(@RequestParam(value = "totalId") String totalId) throws InterruptedException {
        return totalService.keyPricing(totalId);
    }

    @ApiOperation(value = "获取最近有数据的账单",  notes="需要Authorization")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "userId",value = "用户id",dataType = "string",paramType = "query")
    )
    @GetMapping(value = "/getTime")
    public Result getTime(String userId){
        if("".equals(userId) || userId==null){
            userId = totalService.getUserIdStr();
        }
        Total time = totalMapper.getTime(userId);

        if(time==null){
            time=new Total();
            Date date = DateHelp.addDateMonths(new Date(), -1);
            time.setTotalTime(DateHelp.format(date,"yyyy-MM"));
        }
        return Result.ok(time.getTotalTime());
    }
}

