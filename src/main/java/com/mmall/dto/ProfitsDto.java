package com.mmall.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class ProfitsDto {

    @ApiModelProperty(value = "总单量")
    private Integer totalNumber;

    @ApiModelProperty(value = "总重量")
    private BigDecimal totalWeight;

    @ApiModelProperty(value = "平均重量")
    private BigDecimal averageWeight;

    @ApiModelProperty(value = "应收")
    private BigDecimal totalOffer;

    @ApiModelProperty(value = "实收")
    private BigDecimal totalPaid;

    @ApiModelProperty(value = "成本")
    private BigDecimal totalCost;

    @ApiModelProperty(value = "利润")
    private BigDecimal profits;

    @ApiModelProperty(value = "销售单价")
    private BigDecimal price;

    @ApiModelProperty(value = "成本单价")
    private BigDecimal costPrice;


}
