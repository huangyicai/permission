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
 * 每日单量
 * </p>
 *
 * @author qty
 * @since 2018-10-19
 */
@ApiModel(value = "DailyTotal", description = "每日单量")
@TableName("daily_total")
public class DailyTotal implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "daily_id", type = IdType.AUTO)
    private Integer dailyId;
    /**
     * 账单id
     */
    @ApiModelProperty(value = "账单id")
    @TableField("total_id")
    private Integer totalId;
    /**
     * 年月
     */
    @ApiModelProperty(value = "年月")
    @TableField("daily_time")
    private String dailyTime;
    /**
     * 每日单量的记录，逗号隔开
     */
    @ApiModelProperty(value = "每日单量的记录，逗号隔开")
    @TableField("daily_text")
    private String dailyText;


    public Integer getDailyId() {
        return dailyId;
    }

    public void setDailyId(Integer dailyId) {
        this.dailyId = dailyId;
    }

    public Integer getTotalId() {
        return totalId;
    }

    public void setTotalId(Integer totalId) {
        this.totalId = totalId;
    }

    public String getDailyTime() {
        return dailyTime;
    }

    public void setDailyTime(String dailyTime) {
        this.dailyTime = dailyTime;
    }

    public String getDailyText() {
        return dailyText;
    }

    public void setDailyText(String dailyText) {
        this.dailyText = dailyText;
    }

    @Override
    public String toString() {
        return "DailyTotal{" +
        ", dailyId=" + dailyId +
        ", totalId=" + totalId +
        ", dailyTime=" + dailyTime +
        ", dailyText=" + dailyText +
        "}";
    }
}
