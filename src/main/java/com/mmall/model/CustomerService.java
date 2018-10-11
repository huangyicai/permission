package com.mmall.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
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
 * @since 2018-10-09
 */
@ApiModel(value = "CustomerService", description = "客服/工单")
@TableName("customer_service")
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerService implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    @TableField("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    @TableField("user_id")
    private Integer userId;
    /**
     * 所属快递公司id
     */
    @ApiModelProperty(value = "所属快递公司id")
    @TableField("express_id")
    private Integer expressId;
    /**
     * 处理人ID
     */
    @ApiModelProperty(value = "处理人ID")
    @TableField("handle_id")
    private Integer handleId;
    /**
     * 处理人昵称
     */
    @ApiModelProperty(value = "处理人昵称")
    @TableField("handle_name")
    private String handleName;
    /**
     * 运单号
     */
    @ApiModelProperty(value = "运单号")
    @TableField("waybill_number")
    private String waybillNumber;
    /**
     * 问题描述
     */
    @ApiModelProperty(value = "问题描述")
    @TableField("content")
    private String content;
    /**
     * 联络人
     */
    @ApiModelProperty(value = "联络人")
    @TableField("contacts")
    private String contacts;
    /**
     * 联系电话
     */
    @ApiModelProperty(value = "联系电话")
    @TableField("phone")
    private String phone;
    /**
     * 附件地址
     */
    @ApiModelProperty(value = "附件地址")
    @TableField("enclosure")
    private String enclosure;
    /**
     * 时间段
     */
    @ApiModelProperty(value = "时间段")
    @TableField("time_slot")
    private String timeSlot;
    /**
     * 类型（1=破损，2=丢失，3=其他）
     */
    @ApiModelProperty(value = "类型（1=破损，2=丢失，3=其他）")
    @TableField("type_id")
    private Integer typeId;
    /**
     * 类型名称
     */
    @ApiModelProperty(value = "类型名称")
    @TableField("type_name")
    private String typeName;
    /**
     * 状态（1=未处理，2=处理中，3=处理完毕）
     */
    @ApiModelProperty(value = "状态（1=未处理，2=处理中，3=处理完毕）")
    @TableField("status")
    private Integer status;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    @TableField("remarks")
    private String remarks;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @TableField("create_time")
    private String createTime;
    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    @TableField("update_time")
    private String updateTime;


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

    public Integer getExpressId() {
        return expressId;
    }

    public void setExpressId(Integer expressId) {
        this.expressId = expressId;
    }

    public Integer getHandleId() {
        return handleId;
    }

    public void setHandleId(Integer handleId) {
        this.handleId = handleId;
    }

    public String getHandleName() {
        return handleName;
    }

    public void setHandleName(String handleName) {
        this.handleName = handleName;
    }

    public String getWaybillNumber() {
        return waybillNumber;
    }

    public void setWaybillNumber(String waybillNumber) {
        this.waybillNumber = waybillNumber;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEnclosure() {
        return enclosure;
    }

    public void setEnclosure(String enclosure) {
        this.enclosure = enclosure;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public CustomerService() {
    }

    public CustomerService(Integer id, Integer userId, Integer expressId, Integer handleId, String handleName, String waybillNumber, String content, String contacts, String phone, String enclosure, String timeSlot, Integer typeId, String typeName, Integer status, String remarks, String createTime, String updateTime) {
        this.id = id;
        this.userId = userId;
        this.expressId = expressId;
        this.handleId = handleId;
        this.handleName = handleName;
        this.waybillNumber = waybillNumber;
        this.content = content;
        this.contacts = contacts;
        this.phone = phone;
        this.enclosure = enclosure;
        this.timeSlot = timeSlot;
        this.typeId = typeId;
        this.typeName = typeName;
        this.status = status;
        this.remarks = remarks;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "CustomerService{" +
        ", id=" + id +
        ", userId=" + userId +
        ", expressId=" + expressId +
        ", handleId=" + handleId +
        ", handleName=" + handleName +
        ", waybillNumber=" + waybillNumber +
        ", content=" + content +
        ", contacts=" + contacts +
        ", phone=" + phone +
        ", enclosure=" + enclosure +
        ", timeSlot=" + timeSlot +
        ", typeId=" + typeId +
        ", typeName=" + typeName +
        ", status=" + status +
        ", remarks=" + remarks +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        "}";
    }
}
