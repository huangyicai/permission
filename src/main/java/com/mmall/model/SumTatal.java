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
 * 总账单
 * </p>
 *
 * @author qty
 * @since 2018-10-16
 */
@ApiModel(value = "SumTatal", description = "总账单")
@TableName("sum_tatal")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SumTatal implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "sum_id", type = IdType.AUTO)
    private Integer sumId;
    /**
     * 用户id（老板号）
     */
    @ApiModelProperty(value = "用户id（老板号）")
    @TableField("user_id")
    private Integer userId;
    /**
     * 总账单名字
     */
    @ApiModelProperty(value = "总账单名字")
    @TableField("sum_name")
    private String sumName;
    /**
     * 上传时间
     */
    @ApiModelProperty(value = "上传时间")
    @TableField("sum_time")
    private String sumTime;

    @ApiModelProperty(value = "创建时间")
    @TableField("create_time")
    private String createTime;

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Integer getSumId() {
        return sumId;
    }

    public void setSumId(Integer sumId) {
        this.sumId = sumId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getSumName() {
        return sumName;
    }

    public void setSumName(String sumName) {
        this.sumName = sumName;
    }

    public String getSumTime() {
        return sumTime;
    }

    public void setSumTime(String sumTime) {
        this.sumTime = sumTime;
    }

    @Override
    public String toString() {
        return "SumTatal{" +
        ", sumId=" + sumId +
        ", userId=" + userId +
        ", sumName=" + sumName +
        ", sumTime=" + sumTime +
        "}";
    }
}
