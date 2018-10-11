package com.mmall.model.params;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

@Getter
@Setter
public class PaymentMethodParam {

    /**
     * 付款方式
     */
    @ApiModelProperty(value = "付款方式")
    @NotBlank(message = "付款方式不可以为空")
    private String typeName;
    /**
     * 收款人
     */
    @ApiModelProperty(value = "收款人")
    @NotBlank(message = "收款人不可以为空")
    private String payee;
    /**
     * 付款账号
     */
    @ApiModelProperty(value = "付款账号")
    @NotBlank(message = "付款账号不可以为空")
    private String paymentAccount;
}
