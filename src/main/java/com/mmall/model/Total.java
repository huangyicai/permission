package com.mmall.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * <p>
 * 月计表(客户账单)
 * </p>
 *
 * @author qty
 * @since 2018-09-25
 */
@ApiModel(value = "Total", description = "月计表(客户账单)")
@TableName("total")
@Builder
public class Total implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "total_id", type = IdType.AUTO)
    private Integer totalId;

    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户id")
    @TableField("user_id")
    private Integer userId;

    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名")
    @TableField("name")
    private String name;
    /**
     * 时间
     */
    @ApiModelProperty(value = "时间")
    @TableField("total_time")
    private String totalTime;
    /**
     * 总件数
     */
    @ApiModelProperty(value = "总件数")
    @TableField("total_number")
    private Integer totalNumber;
    /**
     * 总重量
     */
    @ApiModelProperty(value = "总重量")
    @TableField("total_weight")
    private BigDecimal totalWeight;
    /**
     * 成本
     */
    @ApiModelProperty(value = "成本")
    @TableField("total_cost")
    private BigDecimal totalCost;
    /**
     * 报价
     */
    @ApiModelProperty(value = "报价")
    @TableField("total_offer")
    private BigDecimal totalOffer;
    /**
     * 实收
     */
    @ApiModelProperty(value = "实收")
    @TableField("total_paid")
    private BigDecimal totalPaid;
    /**
     * 账单储存地址
     */
    @ApiModelProperty(value = "账单储存地址")
    @TableField("total_url")
    private String totalUrl;
    /**
     * 是否结账：1-否，2-是
     */
    @ApiModelProperty(value = "是否结账：1-否，2-是")
    @TableField("total_state")
    private Integer totalState;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @TableField("create_time")
    private Date createTime;
    /**
     * 创建ip
     */
    @ApiModelProperty(value = "创建ip")
    @TableField("create_ip")
    private String createIp;
    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间")
    @TableField("update_time")
    private Date updateTime;
    /**
     * 修改ip
     */
    @ApiModelProperty(value = "修改ip")
    @TableField("update_ip")
    private String updateIp;


    public Integer getTotalId() {
        return totalId;
    }

    public void setTotalId(Integer totalId) {
        this.totalId = totalId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
    }

    public Integer getTotalNumber() {
        return totalNumber;
    }

    public void setTotalNumber(Integer totalNumber) {
        this.totalNumber = totalNumber;
    }

    public BigDecimal getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(BigDecimal totalWeight) {
        this.totalWeight = totalWeight;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public BigDecimal getTotalOffer() {
        return totalOffer;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setTotalOffer(BigDecimal totalOffer) {
        this.totalOffer = totalOffer;
    }

    public BigDecimal getTotalPaid() {
        return totalPaid;
    }

    public void setTotalPaid(BigDecimal totalPaid) {
        this.totalPaid = totalPaid;
    }

    public String getTotalUrl() {
        return totalUrl;
    }

    public void setTotalUrl(String totalUrl) {
        this.totalUrl = totalUrl;
    }

    public Integer getTotalState() {
        return totalState;
    }

    public void setTotalState(Integer totalState) {
        this.totalState = totalState;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateIp() {
        return createIp;
    }

    public void setCreateIp(String createIp) {
        this.createIp = createIp;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateIp() {
        return updateIp;
    }

    public void setUpdateIp(String updateIp) {
        this.updateIp = updateIp;
    }

    @Override
    public String toString() {
        return "Total{" +
        ", totalId=" + totalId +
        ", name=" + name +
        ", totalTime=" + totalTime +
        ", totalNumber=" + totalNumber +
        ", totalWeight=" + totalWeight +
        ", totalCost=" + totalCost +
        ", totalOffer=" + totalOffer +
        ", totalPaid=" + totalPaid +
        ", totalUrl=" + totalUrl +
        ", totalState=" + totalState +
        ", createTime=" + createTime +
        ", createIp=" + createIp +
        ", updateTime=" + updateTime +
        ", updateIp=" + updateIp +
        "}";
    }

    public Total() {
    }

    public Total(Integer totalId, Integer userId, String name, String totalTime, Integer totalNumber, BigDecimal totalWeight, BigDecimal totalCost, BigDecimal totalOffer, BigDecimal totalPaid, String totalUrl, Integer totalState, Date createTime, String createIp, Date updateTime, String updateIp) {
        this.totalId = totalId;
        this.userId = userId;
        this.name = name;
        this.totalTime = totalTime;
        this.totalNumber = totalNumber;
        this.totalWeight = totalWeight;
        this.totalCost = totalCost;
        this.totalOffer = totalOffer;
        this.totalPaid = totalPaid;
        this.totalUrl = totalUrl;
        this.totalState = totalState;
        this.createTime = createTime;
        this.createIp = createIp;
        this.updateTime = updateTime;
        this.updateIp = updateIp;
    }
}
