package com.mmall.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
public class BillDto {

    @ApiModelProperty(value = "总单量")
    private Integer totalNumber;

    @ApiModelProperty(value = "总重量")
    private BigDecimal totalWeight;

    @ApiModelProperty(value = "平均重量")
    private BigDecimal averageWeight;

    @ApiModelProperty(value = "每日单量")
    private Integer dailyNum;
}
