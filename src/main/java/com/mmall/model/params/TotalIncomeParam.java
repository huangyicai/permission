package com.mmall.model.params;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TotalIncomeParam {

    /**
     * 已收款
     */
    @ApiModelProperty(value = "已收金额")
    private BigDecimal totalPaid=BigDecimal.ZERO;

    /**
     * 未收款
     */
    @ApiModelProperty(value = "未收金额")
    private BigDecimal totalOffer=BigDecimal.ZERO;
}
