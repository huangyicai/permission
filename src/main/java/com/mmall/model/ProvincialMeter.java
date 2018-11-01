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
 * 省计表（新）
 * </p>
 *
 * @author qty
 * @since 2018-10-29
 */
@ApiModel(value = "ProvincialMeter", description = "省计表（新）")
@TableName("provincial_meter")
public class ProvincialMeter implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "meter_id", type = IdType.AUTO)
    private Integer meterId;
    /**
     * 总账单
     */
    @ApiModelProperty(value = "总账单")
    @TableField("total_id")
    private Integer totalId;
    /**
     * 数据
     */
    @ApiModelProperty(value = "数据")
    @TableField("meter_text")
    private String meterText;


    public Integer getMeterId() {
        return meterId;
    }

    public void setMeterId(Integer meterId) {
        this.meterId = meterId;
    }

    public Integer getTotalId() {
        return totalId;
    }

    public void setTotalId(Integer totalId) {
        this.totalId = totalId;
    }

    public String getMeterText() {
        return meterText;
    }

    public void setMeterText(String meterText) {
        this.meterText = meterText;
    }

    @Override
    public String toString() {
        return "ProvincialMeter{" +
        ", meterId=" + meterId +
        ", totalId=" + totalId +
        ", meterText=" + meterText +
        "}";
    }

    public ProvincialMeter() {
    }
}
