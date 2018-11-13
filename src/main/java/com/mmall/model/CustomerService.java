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

    @ApiModelProperty(value = "接单时间")
    @TableField("receive_time")
    private String receiveTime;

    @ApiModelProperty(value = "完结时间")
    @TableField("end_time")
    private String endTime;

    @ApiModelProperty(value = "接单耗时")
    @TableField("receive_time_solt")
    private long receiveTimeSolt;

    @ApiModelProperty(value = "完结耗时")
    @TableField("end_time_solt")
    private long endTimeSolt;

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


    public long getReceiveTimeSolt() {
        return receiveTimeSolt;
    }

    public void setReceiveTimeSolt(long receiveTimeSolt) {
        this.receiveTimeSolt = receiveTimeSolt;
    }

    public long getEndTimeSolt() {
        return endTimeSolt;
    }

    public void setEndTimeSolt(long endTimeSolt) {
        this.endTimeSolt = endTimeSolt;
    }

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

    public String getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(String receiveTime) {
        this.receiveTime = receiveTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
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

    public CustomerService(Integer id, Integer userId, Integer expressId, Integer handleId, String handleName, String waybillNumber, String content, String contacts, String phone, String enclosure, String receiveTime, String endTime, long receiveTimeSolt, long endTimeSolt, Integer typeId, String typeName, Integer status, String remarks, String createTime, String updateTime) {
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
        this.receiveTime = receiveTime;
        this.endTime = endTime;
        this.receiveTimeSolt = receiveTimeSolt;
        this.endTimeSolt = endTimeSolt;
        this.typeId = typeId;
        this.typeName = typeName;
        this.status = status;
        this.remarks = remarks;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CustomerService that = (CustomerService) o;

        if (receiveTimeSolt != that.receiveTimeSolt) return false;
        if (endTimeSolt != that.endTimeSolt) return false;
        if (!id.equals(that.id)) return false;
        if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;
        if (expressId != null ? !expressId.equals(that.expressId) : that.expressId != null) return false;
        if (handleId != null ? !handleId.equals(that.handleId) : that.handleId != null) return false;
        if (handleName != null ? !handleName.equals(that.handleName) : that.handleName != null) return false;
        if (waybillNumber != null ? !waybillNumber.equals(that.waybillNumber) : that.waybillNumber != null)
            return false;
        if (content != null ? !content.equals(that.content) : that.content != null) return false;
        if (contacts != null ? !contacts.equals(that.contacts) : that.contacts != null) return false;
        if (phone != null ? !phone.equals(that.phone) : that.phone != null) return false;
        if (enclosure != null ? !enclosure.equals(that.enclosure) : that.enclosure != null) return false;
        if (receiveTime != null ? !receiveTime.equals(that.receiveTime) : that.receiveTime != null) return false;
        if (endTime != null ? !endTime.equals(that.endTime) : that.endTime != null) return false;
        if (typeId != null ? !typeId.equals(that.typeId) : that.typeId != null) return false;
        if (typeName != null ? !typeName.equals(that.typeName) : that.typeName != null) return false;
        if (status != null ? !status.equals(that.status) : that.status != null) return false;
        if (remarks != null ? !remarks.equals(that.remarks) : that.remarks != null) return false;
        if (createTime != null ? !createTime.equals(that.createTime) : that.createTime != null) return false;
        return updateTime != null ? updateTime.equals(that.updateTime) : that.updateTime == null;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + (expressId != null ? expressId.hashCode() : 0);
        result = 31 * result + (handleId != null ? handleId.hashCode() : 0);
        result = 31 * result + (handleName != null ? handleName.hashCode() : 0);
        result = 31 * result + (waybillNumber != null ? waybillNumber.hashCode() : 0);
        result = 31 * result + (content != null ? content.hashCode() : 0);
        result = 31 * result + (contacts != null ? contacts.hashCode() : 0);
        result = 31 * result + (phone != null ? phone.hashCode() : 0);
        result = 31 * result + (enclosure != null ? enclosure.hashCode() : 0);
        result = 31 * result + (receiveTime != null ? receiveTime.hashCode() : 0);
        result = 31 * result + (endTime != null ? endTime.hashCode() : 0);
        result = 31 * result + (int) (receiveTimeSolt ^ (receiveTimeSolt >>> 32));
        result = 31 * result + (int) (endTimeSolt ^ (endTimeSolt >>> 32));
        result = 31 * result + (typeId != null ? typeId.hashCode() : 0);
        result = 31 * result + (typeName != null ? typeName.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (remarks != null ? remarks.hashCode() : 0);
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        result = 31 * result + (updateTime != null ? updateTime.hashCode() : 0);
        return result;
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
        ", typeId=" + typeId +
        ", typeName=" + typeName +
        ", status=" + status +
        ", remarks=" + remarks +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        "}";
    }
}
