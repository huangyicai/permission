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
 * 预付表
 * </p>
 *
 * @author qty
 * @since 2018-12-12
 */
@ApiModel(value = "Prepaid", description = "预付表")
@TableName("prepaid")
public class Prepaid implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    @TableField("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 用户详情id
     */
    @ApiModelProperty(value = "用户详情id")
    @TableField("user_id")
    private Integer userId;
    /**
     * 预付金额/每单
     */
    @ApiModelProperty(value = "预付金额/每单")
    @TableField("money")
    private BigDecimal money;


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

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    @Override
    public String toString() {
        return "Prepaid{" +
        ", id=" + id +
        ", userId=" + userId +
        ", money=" + money +
        "}";
    }
}
