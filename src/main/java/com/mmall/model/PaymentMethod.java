package com.mmall.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import java.io.Serializable;
import java.math.BigDecimal;


/**
 * <p>
 * 
 * </p>
 *
 * @author hyc
 * @since 2018-10-11
 */
@ApiModel(value = "PaymentMethod", description = "")
@TableName("payment_method")
@Builder
public class PaymentMethod implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    @TableField("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * userID
     */
    @ApiModelProperty(value = "userID")
    @TableField("user_id")
    private Integer userId;
    /**
     * 付款方式
     */
    @ApiModelProperty(value = "付款方式")
    @TableField("type_name")
    private String typeName;
    /**
     * 收款人
     */
    @ApiModelProperty(value = "收款人")
    @TableField("payee")
    private String payee;
    /**
     * 付款账号
     */
    @ApiModelProperty(value = "付款账号")
    @TableField("payment_account")
    private String paymentAccount;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getPayee() {
        return payee;
    }

    public void setPayee(String payee) {
        this.payee = payee;
    }

    public String getPaymentAccount() {
        return paymentAccount;
    }

    public void setPaymentAccount(String paymentAccount) {
        this.paymentAccount = paymentAccount;
    }

    @Override
    public String toString() {
        return "PaymentMethod{" +
        ", id=" + id +
        ", userId=" + userId +
        ", typeName=" + typeName +
        ", payee=" + payee +
        ", paymentAccount=" + paymentAccount +
        "}";
    }
}
