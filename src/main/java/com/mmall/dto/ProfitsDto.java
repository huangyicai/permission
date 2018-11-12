package com.mmall.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class ProfitsDto {

    @ApiModelProperty(value = "总单量")
    private Integer totalNumber=0;

    @ApiModelProperty(value = "每日单量")
    private Integer averageNumber=0;

    @ApiModelProperty(value = "总重量")
    private BigDecimal totalWeight=new BigDecimal(0);

    @ApiModelProperty(value = "平均重量")
    private BigDecimal averageWeight=new BigDecimal(0);

    @ApiModelProperty(value = "应收")
    private BigDecimal totalOffer=new BigDecimal(0);

    @ApiModelProperty(value = "应收成本")
    private BigDecimal offerCost=new BigDecimal(0);

    @ApiModelProperty(value = "应收利润")
    private BigDecimal offerProfits=new BigDecimal(0);

    @ApiModelProperty(value = "应收单间利润")
    private BigDecimal offerOneProfits=new BigDecimal(0);

    @ApiModelProperty(value = "应收单价")
    private BigDecimal offerOnePrice=new BigDecimal(0);

    @ApiModelProperty(value = "应收成本单价")
    private BigDecimal offerCostOne=new BigDecimal(0);

    @ApiModelProperty(value = "应收利润百分比")
    private BigDecimal offerProfitsPercentage=new BigDecimal(0);

    @ApiModelProperty(value = "实收")
    private BigDecimal totalPaid=new BigDecimal(0);

    @ApiModelProperty(value = "实收成本")
    private BigDecimal paidCost=new BigDecimal(0);

    @ApiModelProperty(value = "实收利润")
    private BigDecimal paidProfits=new BigDecimal(0);

    @ApiModelProperty(value = "实收单间利润")
    private BigDecimal paidOneProfits=new BigDecimal(0);

    @ApiModelProperty(value = "实收单价")
    private BigDecimal paidOnePrice=new BigDecimal(0);

    @ApiModelProperty(value = "实收成本单价")
    private BigDecimal paidCostOne=new BigDecimal(0);

    @ApiModelProperty(value = "实收利润百分比")
    private BigDecimal paidProfitsPercentage=new BigDecimal(0);
}
