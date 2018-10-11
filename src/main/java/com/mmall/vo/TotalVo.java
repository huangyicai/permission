package com.mmall.vo;

import com.mmall.model.Total;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class TotalVo extends Total implements Serializable {

    @ApiModelProperty(value = "付款方式")
    private String typeName;

    @ApiModelProperty(value = "收款人")
    private String payee;

    @ApiModelProperty(value = "付款账号")
    private String paymentAccount;
}
