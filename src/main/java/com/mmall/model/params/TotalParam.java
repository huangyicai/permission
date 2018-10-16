package com.mmall.model.params;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import java.math.BigDecimal;

@Getter
@Setter
public class TotalParam {

    @ApiModelProperty(value = "账单id")
    @NotBlank(message = "用户ID不能为空")
    private String totalId;

    @ApiModelProperty(value = "备注")
    private String totalRemark;

    @ApiModelProperty(value = "付款截止时间")
    @NotBlank(message = "用户ID不能为空")
    private String Date;

}
