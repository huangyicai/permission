package com.mmall.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 * <p>
 *
 * </p>
 *
 * @author hyc
 * @since 2018-09-26
 */
@ApiModel(value = "BillKeyword", description = "")
@TableName("bill_keyword")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BillKeyword implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    @TableField("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 关键字
     */
    @ApiModelProperty(value = "关键字")
    @TableField("keyword")
    private String keyword;
    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    @TableField("user_id")
    private Integer userId;
    /**
     * 该记录是否有效1：有效、0：无效
     */
    @ApiModelProperty(value = "该记录是否有效1：有效、0：无效")
    @TableField("status")
    private Integer status;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
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
        return "BillKeyword{" +
                ", id=" + id +
                ", keyword=" + keyword +
                ", userId=" + userId +
                ", status=" + status +
                "}";
    }
}
