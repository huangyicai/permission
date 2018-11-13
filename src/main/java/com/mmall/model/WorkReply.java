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
 * @author qty
 * @since 2018-10-22
 */
@ApiModel(value = "WorkReply", description = "")
@TableName("work_reply")
@Builder
public class WorkReply implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    @TableField("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 回复的用户id
     */
    @ApiModelProperty(value = "回复的用户id")
    @TableField("user_id")
    private Integer userId;
    /**
     * 工单id
     */
    @ApiModelProperty(value = "工单id")
    @TableField("service_id")
    private Integer serviceId;
    /**
     * 回复内容
     */
    @ApiModelProperty(value = "回复内容")
    @TableField("content")
    private String content;
    /**
     * 是否已读
     */
    @ApiModelProperty(value = "客户是否已读")
    @TableField("status")
    private Integer status;

    /**
     * 客服是否已读
     */
    @ApiModelProperty(value = "客服是否已读")
    @TableField("service_type")
    private Integer serviceType;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @TableField("create_time")
    private String  createTime;

    public Integer getServiceType() {
        return serviceType;
    }

    public void setServiceType(Integer serviceType) {
        this.serviceType = serviceType;
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

    public Integer getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public WorkReply() {
    }

    public WorkReply(Integer id, Integer userId, Integer serviceId, String content, Integer status, Integer serviceType, String createTime) {
        this.id = id;
        this.userId = userId;
        this.serviceId = serviceId;
        this.content = content;
        this.status = status;
        this.serviceType = serviceType;
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "WorkReply{" +
        ", id=" + id +
        ", userId=" + userId +
        ", serviceId=" + serviceId +
        ", content=" + content +
        ", status=" + status +
        ", createTime=" + createTime +
        "}";
    }
}
