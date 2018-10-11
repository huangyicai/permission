package com.mmall.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Total implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "total_id", type = IdType.AUTO)
    private Integer totalId;

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    @TableField("user_id")
    private Integer userId;

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    @TableField("send_id")
    private Integer sendId;

    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名")
    @TableField("name")
    private String name;

    /**
     * 用户名
     */
    @ApiModelProperty(value = "单号")
    @TableField("order_no")
    private String orderNo;

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
     *凭证路径
     */
    @ApiModelProperty(value = "凭证路径")
    @TableField("total_credentials_url")
    private String totalCredentialsUrl;
    /**
     * 账单访问地址
     */
    @ApiModelProperty(value = "账单访问地址")
    @TableField("total_url")
    private String totalUrl;

    /**
     * 账单生成地址
     */
    @ApiModelProperty(value = "账单生成地址")
    @TableField("cd_url")
    private String cdUrl;

    /**
     * 是否结账：1-否，2-是
     */
    @ApiModelProperty(value = "是否结账：1-否，2-是")
    @TableField("total_state")
    private Integer totalState;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    @TableField("total_remark")
    private Integer totalRemark;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @TableField("create_time")
    private Date createTime;

    /**
     * 截止时间
     */
    @ApiModelProperty(value = "截止时间")
    @TableField("as_of_time")
    private Date asOfTime;


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

    public String getCdUrl() {
        return cdUrl;
    }

    public void setCdUrl(String cdUrl) {
        this.cdUrl = cdUrl;
    }

    public Integer getTotalRemark() {
        return totalRemark;
    }

    public void setTotalRemark(Integer totalRemark) {
        this.totalRemark = totalRemark;
    }

    public Date getAsOfTime() {
        return asOfTime;
    }

    public void setAsOfTime(Date asOfTime) {
        this.asOfTime = asOfTime;
    }

    public Integer getSendId() {
        return sendId;
    }

    public void setSendId(Integer sendId) {
        this.sendId = sendId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

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

    public String getTotalCredentialsUrl() {
        return totalCredentialsUrl;
    }

    public void setTotalCredentialsUrl(String totalCredentialsUrl) {
        this.totalCredentialsUrl = totalCredentialsUrl;
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

}
