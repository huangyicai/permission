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
 * 消息表
 * </p>
 *
 * @author qty
 * @since 2018-12-06
 */
@ApiModel(value = "Message", description = "消息表")
@TableName("message")
public class Message implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    @TableField("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String title;

    /**
     * 发送者id
     */
    @ApiModelProperty(value = "发送者id")
    @TableField("send_id")
    private Integer sendId;
    /**
     * 内容
     */
    @ApiModelProperty(value = "内容")
    @TableField("content")
    private String content;

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

    public Integer getSendId() {
        return sendId;
    }

    public void setSendId(Integer sendId) {
        this.sendId = sendId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    @Override
    public String toString() {
        return "Message{" +
        ", id=" + id +
        ", title=" + title +
        ", content=" + content +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        "}";
    }

    public Message() {
    }

    public Message(Integer id, String title, Integer sendId, String content, String createTime, String updateTime) {
        this.id = id;
        this.title = title;
        this.sendId = sendId;
        this.content = content;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }
}
