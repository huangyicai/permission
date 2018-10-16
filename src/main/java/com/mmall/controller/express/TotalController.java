package com.mmall.controller.express;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mmall.config.UserInfoConfig;
import com.mmall.dao.SumTatalMapper;
import com.mmall.dao.TotalMapper;
import com.mmall.dto.BillDto;
import com.mmall.dto.ProfitsDto;
import com.mmall.model.Response.InfoEnums;
import com.mmall.model.SumTatal;
import com.mmall.model.SysUserInfo;
import com.mmall.model.params.BillDetailsParam;
import com.mmall.model.params.BillParam;
import com.mmall.model.params.TotalIncomeParam;
import com.mmall.service.SysUserInfoService;
import com.mmall.excel.imp.XlsxProcessAbstract;
import com.mmall.model.Response.Result;
import com.mmall.model.Total;
import com.mmall.model.params.TotalParam;
import com.mmall.service.TotalService;
import com.mmall.vo.TotalVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static java.math.BigDecimal.ROUND_DOWN;

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
    private  XlsxProcessAbstract xlsxProcessAbstract;

    @ApiOperation(value = "确认收款",  notes="需要Authorization")
    @GetMapping(value = "/update/{totalId}/{money}")
    public Result updateSatte(@PathVariable(value = "totalId") Integer totalId,
                              @PathVariable(value = "money") Double money){
        Total total=new Total();
        total.setTotalId(totalId);
        total.setTotalPaid(new BigDecimal(money).setScale(2,ROUND_DOWN));
        total.setTotalState(4);
        total.setUpdateTime(new Date());
        totalService.updateById(total);
        return Result.ok();
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
            totalMapper.updateByTotalId(totalParam.getTotalId(),totalParam.getTotalRemark(),totalParam.getDate());
            return Result.ok();
        }else {
            return Result.error(InfoEnums.SEND_FAILURE);
        }

    }

    @ApiOperation(value = "上传账单---追加",  notes="需要Authorization")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "time",value = "时间",dataType = "String"),
            @ApiImplicitParam(name = "userId",value = "用户id",dataType = "Integer")
    })
    @PostMapping(value = "/additional")
    public Result additional(MultipartFile file,@RequestParam("time") String time) throws Exception {

        //创建文件写入路径
        xlsxProcessAbstract.processAllSheet(file,time,1,null);
        return Result.ok();
    }

    @ApiOperation(value = "上传账单---替换",  notes="需要Authorization")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "time",value = "时间",dataType = "String"),
            @ApiImplicitParam(name = "userId",value = "用户id",dataType = "Integer"),
            @ApiImplicitParam(name = "sumId",value = "总账单id",dataType = "String")
    })
    @PostMapping(value = "/replace")
    public Result replace(MultipartFile file,
                          @RequestParam("time") String time,
                          @RequestParam("sumId") String sumId) throws Exception {

        //创建文件写入路径
        xlsxProcessAbstract.processAllSheet(file,time,2,sumId);
        return Result.ok();
    }

    @ApiOperation(value = "替换账单--检测是否有已经发送的订单",  notes="需要Authorization")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "time",value = "时间",dataType = "String"),
            @ApiImplicitParam(name = "sumId",value = "总账单id",dataType = "String")
    })
    @PostMapping(value = "/retrieve")
    public Result retrieve(@RequestParam("time") String time,
                          @RequestParam("sumId") String sumId){

        List<Total> totals = totalMapper.listTotal(time, sumId);
        return Result.ok(totals);
    }

    @ApiOperation(value = "客户追加上传",  notes="需要Authorization")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "time",value = "时间",dataType = "String"),
            @ApiImplicitParam(name = "type",value = "类型：1-已经定价，2-未定价",dataType = "Integer"),
            @ApiImplicitParam(name = "userId",value = "用户id",dataType = "Integer"),
    })
    @PostMapping(value = "/additionalSet")
    public Result additionalSet(MultipartFile file,
                                @RequestParam("time") String time,
                                @RequestParam("type") Integer type,@RequestParam("userId") Integer userId) throws Exception {
        xlsxProcessAbstract.additionalSet(file,userId,type,time);
        return Result.ok();
    }

    @ApiOperation(value = "获取该月总账单",  notes="需要Authorization")
    @PostMapping(value = "/judgeSet")
    public Result<List<SumTatal>> judgeSet(@RequestParam("time") String time) throws Exception {
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
    public Result<String> getPricing(@PathVariable("totalId")Integer totalId){
        return totalService.getPricing(totalId);
    }

    @ApiOperation(value = "重新上传",  notes="需要Authorization")
    @PostMapping(value = "/againSet/{totalId}")
    public Result<String> againSet(@PathVariable("totalId")Integer totalId,
                                   MultipartFile file) throws Exception {
        xlsxProcessAbstract.againSet(file,totalId);
        return Result.ok();
    }

    @ApiOperation(value = "轮询账单表",  notes="需要Authorization")
    @GetMapping(produces = {"application/json;charest=Utf-8"})
    public Result polling(@RequestParam("time") String time){
        SysUserInfo user = UserInfoConfig.getUserInfo();
        return totalService.polling(time,user.getId());
    }
}

