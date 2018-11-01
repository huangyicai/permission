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
 * @since 2018-10-30
 */
@ApiModel(value = "UseTerm", description = "用户使用软件期限")
@TableName("use_term")
@Builder
public class UseTerm implements Serializable {

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
     * 截止日期时间戳
     */
    @ApiModelProperty(value = "截止日期时间戳")
    @TableField("closing_date")
    private String closingDate;
    /**
     * 开始日期时间戳
     */
    @ApiModelProperty(value = "开始日期时间戳")
    @TableField("start_date")
    private String startDate;


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

    public String getClosingDate() {
        return closingDate;
    }

    public void setClosingDate(String closingDate) {
        this.closingDate = closingDate;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public UseTerm() {
    }

    public UseTerm(Integer id, Integer userId, String closingDate, String startDate) {
        this.id = id;
        this.userId = userId;
        this.closingDate = closingDate;
        this.startDate = startDate;
    }

    @Override
    public String toString() {
        return "UseTerm{" +
        ", id=" + id +
        ", userId=" + userId +
        ", closingDate=" + closingDate +
        ", startDate=" + startDate +
        "}";
    }
}
