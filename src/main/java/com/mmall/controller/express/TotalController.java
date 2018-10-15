package com.mmall.controller.express;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mmall.config.UserInfoConfig;
import com.mmall.dto.BillDto;
import com.mmall.dto.ProfitsDto;
import com.mmall.model.Response.InfoEnums;
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
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

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
    private SysUserInfoService sysUserInfoService;

    @Autowired
    private  XlsxProcessAbstract xlsxProcessAbstract;

    @ApiOperation(value = "确认收款",  notes="需要Authorization")
    @GetMapping(value = "/update")
    public Result updateSatte(TotalParam totalParam){
        Total total=new Total();
        total.setTotalId(totalParam.getTotalId());
        total.setTotalPaid(totalParam.getMoney());
        total.setTotalState(2);
        total.setUpdateTime(new Date());
        totalService.updateById(total);
        return Result.ok();
    }

    @ApiOperation(value = "修改账单(暂时不适用)",  notes="需要Authorization")
    @GetMapping(value = "/updateMoney")
    public Result updateMoney(TotalParam totalParam){
        Total total=new Total();
        total.setTotalId(totalParam.getTotalId());
        total.setTotalPaid(totalParam.getMoney());
        totalService.updateById(total);
        return Result.ok();
    }

    @ApiOperation(value = "发送账单",notes = "需要Authorization")
    @PostMapping(value = "/send",produces = {"application/json;charest=Utf-8"})
    public Result send(@RequestBody TotalParam totalParam){
        Total byId = totalService.getById(totalParam.getTotalId());
        if(byId.getTotalState()==1){
            Total total=new Total();
            total.setTotalId(totalParam.getTotalId());
            total.setTotalState(totalParam.getState());
            total.setTotalRemark(totalParam.getTotalRemark());
            total.setTotalState(2);
            totalService.updateById(total);
            return Result.ok();
        }else {
            return Result.error(InfoEnums.SEND_FAILURE);
        }

    }

    @ApiOperation(value = "上传账单",  notes="需要Authorization")
    @PostMapping(value = "/set")
    public Result set(MultipartFile file,@RequestParam("time") String time) throws Exception {

        //创建文件写入路径
        String realPath = "C:\\Program Files\\apache-tomcat-9.0.12\\webapps\\total\\";
        xlsxProcessAbstract.processAllSheet(file,time,realPath);
        return Result.ok();
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
        IPage<TotalVo> bill = totalService.getBill(page,billDetailsParam);
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

