package com.mmall.controller.express;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mmall.config.UserInfoConfig;
import com.mmall.dto.BillDto;
import com.mmall.dto.ProfitsDto;
import com.mmall.excel.Bill;
import com.mmall.excel.export.DataSheetExecute;
import com.mmall.excel.export.ExcelExportExecutor;
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
import com.mmall.util.DateUtils;
import com.mmall.util.LevelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
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
    private SysUserInfoService sysUserInfoService;

    @Autowired
    private  XlsxProcessAbstract xlsxProcessAbstract;

    @ApiOperation(value = "获取客户账单(上传账单页面的列表)",  notes="需要Authorization")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "state", value = "状态：1-未发送，2-待确认，3-已付款（未确认，这里不做展示），4-已收款", required = false, dataType = "Integer", paramType = "query")
    )
    @PostMapping(value = "/getList")
    public Result<IPage<Total>> getList(Page page, BillParam billParam,Integer state) throws ParseException {
        QueryWrapper<Total> queryWrapper = new QueryWrapper<Total>();
        queryWrapper
                .eq("total_time", billParam.getDate())
                .in("user_id",billParam.getUserId())
                .eq(state!=0,"total_state",state)
                .orderByAsc(false, "total_state")
                .orderByAsc(false,"total_time");
        IPage<Total> page1 = totalService.page(page, queryWrapper);
        return Result.ok(page1);
    }

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

    @ApiOperation(value = "修改账单",  notes="需要Authorization")
    @GetMapping(value = "/updateMoney")
    public Result updateMoney(TotalParam totalParam){
        Total total=new Total();
        total.setTotalId(totalParam.getTotalId());
        total.setTotalPaid(totalParam.getMoney());
        totalService.updateById(total);
        return Result.ok();
    }

    @ApiOperation(value = "上传账单",  notes="需要Authorization")
    @PostMapping(value = "/set")
    public Result set(MultipartFile file,@RequestParam("time") String time) throws Exception {
        xlsxProcessAbstract.processAllSheet(file,time);
        return Result.ok();
    }

    @ApiOperation(value = "获取账单月份总计",  notes="需要Authorization")
    @PostMapping(value = "/getBillData")
    public Result<BillDto> getBillData(@RequestBody BillParam billParam){

        if(billParam.getUserId()==null||"".equals(billParam.getUserId())){
            SysUserInfo user = UserInfoConfig.getUserInfo();
            String s = LevelUtil.calculateLevel(user.getLevel(), user.getId());
            List<SysUserInfo> list1 = sysUserInfoService.list(new QueryWrapper<SysUserInfo>()
                    .like("level", s)
                    .eq("platform_id", 3)
                    .select("id"));

            String nameStr="";
            for(SysUserInfo sysUserInfo: list1){
                nameStr+=sysUserInfo.getId()+",";
            }

            nameStr=nameStr.substring(0,nameStr.length()-1);

            billParam.setUserId(nameStr);
        }

        Total one = totalService.getToal( billParam.getDate(), billParam.getUserId());
        if(one==null){
           return Result.error(InfoEnums.DATA_IS_NULL);
        }
        BillDto billDto=new BillDto();
        billDto.setTotalNumber(one.getTotalNumber());
        billDto.setTotalWeight(one.getTotalWeight());
        billDto.setAverageWeight(one.getTotalWeight().divide(new BigDecimal(one.getTotalNumber()),2, RoundingMode.DOWN));

        int days = DateUtils.getDays(one.getTotalTime());
        billDto.setDailyNum(one.getTotalNumber()/days);
        return Result.ok(billDto);
    }

    @ApiOperation(value = "获取账单详情（数据分析的账单列表）",  notes="需要Authorization")
    @PostMapping(value = "/getBill")
    public Result<Page<Total>> getBill(Page<Total> page,@RequestBody BillDetailsParam billDetailsParam){

        page.setCurrent(billDetailsParam.getCurrent());
        page.setSize(billDetailsParam.getSize());
        SysUserInfo user = UserInfoConfig.getUserInfo();
        String s = LevelUtil.calculateLevel(user.getLevel(), user.getId());
        List<SysUserInfo> list1 = sysUserInfoService.list(new QueryWrapper<SysUserInfo>()
                .like("level", s)
                .eq("platform_id", 3)
                .select("id"));

        String nameStr="";
        for(SysUserInfo sysUserInfo: list1){
            nameStr+=sysUserInfo.getId()+",";
        }

        nameStr=nameStr.substring(0,nameStr.length()-1);
        List<Total> bill = totalService.getBill(billDetailsParam.getDate(), nameStr, billDetailsParam.getState());
        page.setRecords(bill);
        return Result.ok(page);
    }

    @ApiOperation(value = "获取账单详情：已收入和未收入",  notes="需要Authorization")
    @PostMapping(value = "/getIncome")
    public Result<TotalIncomeParam> getIncome(@RequestBody BillDetailsParam billDetailsParam){

        SysUserInfo user = UserInfoConfig.getUserInfo();
        String s = LevelUtil.calculateLevel(user.getLevel(), user.getId());
        List<SysUserInfo> list1 = sysUserInfoService.list(new QueryWrapper<SysUserInfo>()
                .like("level", s)
                .eq("platform_id", 3)
                .select("id"));

        String nameStr="";
        for(SysUserInfo sysUserInfo: list1){
            nameStr+=sysUserInfo.getId()+",";
        }

        nameStr=nameStr.substring(0,nameStr.length()-1);


        TotalIncomeParam billCount = totalService.getBillCount(billDetailsParam.getDate(), nameStr);
        return Result.ok(billCount);
    }

    @ApiOperation(value = "利润分析",  notes="需要Authorization")
    @PostMapping(value = "/getProfits")
    public Result<ProfitsDto> getProfits(@RequestBody BillParam billParam){

        if(billParam.getUserId()==null||"".equals(billParam.getUserId())){
            SysUserInfo user = UserInfoConfig.getUserInfo();
            String s = LevelUtil.calculateLevel(user.getLevel(), user.getId());
            List<SysUserInfo> list1 = sysUserInfoService.list(new QueryWrapper<SysUserInfo>()
                    .like("level", s)
                    .eq("platform_id", 3)
                    .select("id"));

            String nameStr="";
            for(SysUserInfo sysUserInfo: list1){
                nameStr+=sysUserInfo.getId()+",";
            }

            nameStr=nameStr.substring(0,nameStr.length()-1);

            billParam.setUserId(nameStr);
        }

        Total one = totalService.getToal( billParam.getDate(), billParam.getUserId());

        if(one==null){
            return Result.error(InfoEnums.DATA_IS_NULL);
        }

        ProfitsDto billDto=new ProfitsDto();
        billDto.setTotalNumber(one.getTotalNumber());
        billDto.setTotalWeight(one.getTotalWeight());
        billDto.setAverageWeight(one.getTotalWeight().divide(new BigDecimal(one.getTotalNumber()),2, RoundingMode.DOWN));
        billDto.setTotalOffer(one.getTotalOffer());
        billDto.setTotalPaid(one.getTotalPaid());
        billDto.setTotalCost(one.getTotalCost());
        billDto.setProfits(one.getTotalPaid().subtract(one.getTotalCost()));
        billDto.setPrice(one.getTotalOffer().divide(new BigDecimal(one.getTotalNumber()),2,RoundingMode.DOWN));
        billDto.setCostPrice(one.getTotalCost().divide(new BigDecimal(one.getTotalNumber()),2,RoundingMode.DOWN));


        int days = DateUtils.getDays(one.getTotalTime());
        billDto.setAverageNumber(one.getTotalNumber()/days);

        return Result.ok(billDto);
    }
}

