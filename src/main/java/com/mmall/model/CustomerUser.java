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
 * 客服绑定客户表
 * </p>
 *
 * @author qty
 * @since 2018-12-07
 */
@ApiModel(value = "CustomerUser", description = "客服绑定客户表")
@TableName("customer_user")
public class CustomerUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    @TableField("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 客服id
     */
    @ApiModelProperty(value = "客服id")
    @TableField("customer_id")
    private Integer customerId;
    /**
     * 客户id
     */
    @ApiModelProperty(value = "客户id")
    @TableField("user_id")
    private Integer userId;
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

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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
        return "CustomerUser{" +
        ", id=" + id +
        ", customerId=" + customerId +
        ", userId=" + userId +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        "}";
    }
}
