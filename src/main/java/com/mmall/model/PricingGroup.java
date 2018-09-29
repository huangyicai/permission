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
 * @since 2018-09-28
 */
@ApiModel(value = "PricingGroup", description = "定价组")
@TableName("pricing_group")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PricingGroup implements Serializable {

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
     * 省份id
     */
    @ApiModelProperty(value = "省份id")
    @TableField("city_id")
    private Integer cityId;
    /**
     * 开始区间
     */
    @ApiModelProperty(value = "开始区间")
    @TableField("area_begin")
    private Integer areaBegin;
    /**
     * 结束区间
     */
    @ApiModelProperty(value = "结束区间")
    @TableField("area_end")
    private Integer areaEnd;
    /**
     * 重量标准
     */
    @ApiModelProperty(value = "重量标准")
    @TableField("weight_standard")
    private Integer weightStandard;
    /**
     * 价格
     */
    @ApiModelProperty(value = "价格")
    @TableField("price")
    private Integer price;
    /**
     * 1=首重，2=续重
     */
    @ApiModelProperty(value = "1=首重，2=续重")
    @TableField("first_or_continued")
    private Integer firstOrContinued;


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

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public Integer getAreaBegin() {
        return areaBegin;
    }

    public void setAreaBegin(Integer areaBegin) {
        this.areaBegin = areaBegin;
    }

    public Integer getAreaEnd() {
        return areaEnd;
    }

    public void setAreaEnd(Integer areaEnd) {
        this.areaEnd = areaEnd;
    }

    public Integer getWeightStandard() {
        return weightStandard;
    }

    public void setWeightStandard(Integer weightStandard) {
        this.weightStandard = weightStandard;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getFirstOrContinued() {
        return firstOrContinued;
    }

    public void setFirstOrContinued(Integer firstOrContinued) {
        this.firstOrContinued = firstOrContinued;
    }

    @Override
    public String toString() {
        return "PricingGroup{" +
        ", id=" + id +
        ", userId=" + userId +
        ", cityId=" + cityId +
        ", areaBegin=" + areaBegin +
        ", areaEnd=" + areaEnd +
        ", weightStandard=" + weightStandard +
        ", price=" + price +
        ", firstOrContinued=" + firstOrContinued +
        "}";
    }
}
