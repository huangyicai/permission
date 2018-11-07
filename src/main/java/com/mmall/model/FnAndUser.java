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
 * 弗恩客服负责人与客户关联表
 * </p>
 *
 * @author hyc
 * @since 2018-11-07
 */
@ApiModel(value = "FnAndUser", description = "弗恩客服负责人与客户关联表")
@TableName("fn_and_user")
@Builder
public class FnAndUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    @TableField("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * userID
     */
    @ApiModelProperty(value = "userID")
    @TableField("user_id")
    private Integer userId;
    /**
     * 负责人
     */
    @ApiModelProperty(value = "负责人")
    @TableField("fn_id")
    private Integer fnId;


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

    public Integer getFnId() {
        return fnId;
    }

    public void setFnId(Integer fnId) {
        this.fnId = fnId;
    }

    public FnAndUser(Integer id, Integer userId, Integer fnId) {
        this.id = id;
        this.userId = userId;
        this.fnId = fnId;
    }

    public FnAndUser() {
    }

    @Override
    public String toString() {
        return "FnAndUser{" +
        ", id=" + id +
        ", userId=" + userId +
        ", fnId=" + fnId +
        "}";
    }
}
