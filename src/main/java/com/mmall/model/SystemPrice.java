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
 * 百里百里系统价格
 * </p>
 *
 * @author qty
 * @since 2018-10-30
 */
@ApiModel(value = "SystemPrice", description = "百里百里系统价格")
@TableName("system_price")
@Builder
public class SystemPrice implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    @TableField("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 月数
     */
    @ApiModelProperty(value = "月数")
    @TableField("month_num")
    private Integer monthNum;
    /**
     * 原价
     */
    @ApiModelProperty(value = "原价")
    @TableField("original_price")
    private Double originalPrice;
    /**
     * 优惠名称
     */
    @ApiModelProperty(value = "优惠名称")
    @TableField("actual_name")
    private String actualName;
    /**
     * 实际价格
     */
    @ApiModelProperty(value = "实际价格")
    @TableField("actual_price")
    private Double actualPrice;
    /**
     * 1=可用，2=不可用，3=删除
     */
    @ApiModelProperty(value = "1=可用，2=不可用，3=删除")
    @TableField("status")
    private Integer status;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMonthNum() {
        return monthNum;
    }

    public void setMonthNum(Integer monthNum) {
        this.monthNum = monthNum;
    }

    public Double getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(Double originalPrice) {
        this.originalPrice = originalPrice;
    }

    public String getActualName() {
        return actualName;
    }

    public void setActualName(String actualName) {
        this.actualName = actualName;
    }

    public Double getActualPrice() {
        return actualPrice;
    }

    public void setActualPrice(Double actualPrice) {
        this.actualPrice = actualPrice;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "SystemPrice{" +
        ", id=" + id +
        ", monthNum=" + monthNum +
        ", originalPrice=" + originalPrice +
        ", actualName=" + actualName +
        ", actualPrice=" + actualPrice +
        ", status=" + status +
        "}";
    }
}
