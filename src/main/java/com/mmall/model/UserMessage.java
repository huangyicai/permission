package com.mmall.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;


/**
 * <p>
 * 用户消息关联表
 * </p>
 *
 * @author qty
 * @since 2018-12-07
 */
@ApiModel(value = "UserMessage", description = "用户消息关联表")
@TableName("user_message")
public class UserMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    @TableField("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 信息id
     */
    @ApiModelProperty(value = "信息id")
    @TableField("message_id")
    private Integer messageId;
    /**
     * 发送者id
     */
    @ApiModelProperty(value = "发送者id")
    @TableField("send_id")
    private Integer sendId;
    /**
     * 接收者id
     */
    @ApiModelProperty(value = "接收者id")
    @TableField("user_id")
    private Integer userId;
    /**
     * 状态：0-已读，1-未读
     */
    @ApiModelProperty(value = "状态：0-已读，1-未读")
    @TableField("status")
    private Integer status;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMessageId() {
        return messageId;
    }

    public void setMessageId(Integer messageId) {
        this.messageId = messageId;
    }

    public Integer getSendId() {
        return sendId;
    }

    public void setSendId(Integer sendId) {
        this.sendId = sendId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "UserMessage{" +
        ", id=" + id +
        ", messageId=" + messageId +
        ", sendId=" + sendId +
        ", userId=" + userId +
        ", status=" + status +
        "}";
    }
}
