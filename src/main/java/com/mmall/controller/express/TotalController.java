package com.mmall.controller.express;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mmall.dto.BillDto;
import com.mmall.dto.ProfitsDto;
import com.mmall.excel.Bill;
import com.mmall.excel.export.DataSheetExecute;
import com.mmall.excel.export.ExcelExportExecutor;
import com.mmall.model.SysUserInfo;
import com.mmall.model.params.BillParam;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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

    @ApiOperation(value = "获取客户账单",  notes="需要Authorization")
    @PostMapping(value = "/getList")
    public Result<IPage<Total>> getList(Page page, BillParam billParam) throws ParseException {
        QueryWrapper<Total> queryWrapper = new QueryWrapper<Total>();
        queryWrapper
                .eq("total_time", billParam.getDate())
                .eq("user_id",billParam.getId())
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
    public Result<BillDto> getBillData(BillParam billParam){
        Total one = totalService.getOne(new QueryWrapper<Total>().eq("total_time", billParam.getDate()).eq("user_id", billParam.getId()));
        BillDto billDto=new BillDto();
        billDto.setTotalNumber(one.getTotalNumber());
        billDto.setTotalWeight(one.getTotalWeight());
        billDto.setAverageWeight(one.getTotalWeight().divide(new BigDecimal(one.getTotalNumber()),2, RoundingMode.DOWN));

        int days = DateUtils.getDays(one.getTotalTime());
        billDto.setDailyNum(one.getTotalNumber()/days);
        return Result.ok(billDto);
    }

    @ApiOperation(value = "获取账单详情",  notes="需要Authorization")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "date", value = "时间", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "state", value = "是否付款：1-否，2-是", required = false, dataType = "Integer", paramType = "query")
    })
    @PostMapping(value = "/getBill")
    public Result<IPage<Total>> getBill(Page page,String date,Integer state){
        IPage<Total> ipage = totalService.page(page, new QueryWrapper<Total>()
                .eq("total_time", date)
                .eq(state!=0 && state!=null,"total_state",state)
                .select("total_id","name","update_time","create_time","total_offer"));
        return Result.ok(ipage);
    }

    @ApiOperation(value = "获取账单详情：已收入和未收入",  notes="需要Authorization")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "date", value = "时间", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "state", value = "是否付款：1-否，2-是", required = false, dataType = "Integer", paramType = "query")
    })
    @PostMapping(value = "/getIncome")
    public Result getIncome(String date,Integer state){
        QueryWrapper<Total> select1 = new QueryWrapper<Total>();

        select1.eq("total_time", date);

        if(state==1){
            select1 .eq( "total_state", 1)
                    .select("total_paid");
        }
        if(state==2){
            select1 .eq( "total_state", 2)
                    .select("total_offer");
        }
        int count = totalService.count(select1);
        return Result.ok(count);
    }

    @ApiOperation(value = "利润分析",  notes="需要Authorization")
    @PostMapping(value = "/getProfits")
    public Result<ProfitsDto> getProfits(BillParam billParam){
        Total one = totalService.getOne(new QueryWrapper<Total>().eq("total_time", billParam.getDate()).eq("name", billParam.getId()));
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

        return Result.ok(billDto);
    }
}

