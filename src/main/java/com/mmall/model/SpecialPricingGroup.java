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
 * @author hyc
 * @since 2018-10-14
 */
@ApiModel(value = "SpecialPricingGroup", description = "特殊定价组")
@TableName("special_pricing_group")
@Builder
public class SpecialPricingGroup implements Serializable {

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
    @TableField("key_id")
    private Integer keyId;
    /**
     * 开始区间
     */
    @ApiModelProperty(value = "开始区间")
    @TableField("area_begin")
    private Double areaBegin;
    /**
     * 结束区间
     */
    @ApiModelProperty(value = "结束区间")
    @TableField("area_end")
    private Double areaEnd;
    /**
     * 重量标准
     */
    @ApiModelProperty(value = "重量标准")
    @TableField("weight_standard")
    private Double weightStandard;
    /**
     * 续重的首重价格
     */
    @ApiModelProperty(value = "续重的首重价格")
    @TableField("first_weight_price")
    private Double firstWeightPrice;
    /**
     * 续重的首重
     */
    @ApiModelProperty(value = "续重的首重")
    @TableField("first_weight")
    private Double firstWeight;
    /**
     * 价格
     */
    @ApiModelProperty(value = "价格")
    @TableField("price")
    private Double price;
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

    public Integer getKeyId() {
        return keyId;
    }

    public void setKeyId(Integer keyId) {
        this.keyId = keyId;
    }

    public Double getAreaBegin() {
        return areaBegin;
    }

    public void setAreaBegin(Double areaBegin) {
        this.areaBegin = areaBegin;
    }

    public Double getAreaEnd() {
        return areaEnd;
    }

    public void setAreaEnd(Double areaEnd) {
        this.areaEnd = areaEnd;
    }

    public Double getWeightStandard() {
        return weightStandard;
    }

    public void setWeightStandard(Double weightStandard) {
        this.weightStandard = weightStandard;
    }

    public Double getFirstWeightPrice() {
        return firstWeightPrice;
    }

    public void setFirstWeightPrice(Double firstWeightPrice) {
        this.firstWeightPrice = firstWeightPrice;
    }

    public Double getFirstWeight() {
        return firstWeight;
    }

    public void setFirstWeight(Double firstWeight) {
        this.firstWeight = firstWeight;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getFirstOrContinued() {
        return firstOrContinued;
    }

    public void setFirstOrContinued(Integer firstOrContinued) {
        this.firstOrContinued = firstOrContinued;
    }

    public SpecialPricingGroup() {
    }

    public SpecialPricingGroup(Integer id, Integer keyId, Double areaBegin, Double areaEnd, Double weightStandard, Double firstWeightPrice, Double firstWeight, Double price, Integer firstOrContinued) {
        this.id = id;
        this.keyId = keyId;
        this.areaBegin = areaBegin;
        this.areaEnd = areaEnd;
        this.weightStandard = weightStandard;
        this.firstWeightPrice = firstWeightPrice;
        this.firstWeight = firstWeight;
        this.price = price;
        this.firstOrContinued = firstOrContinued;
    }

    @Override
    public String toString() {
        return "SpecialPricingGroup{" +
        ", id=" + id +
        ", keyId=" + keyId +
        ", areaBegin=" + areaBegin +
        ", areaEnd=" + areaEnd +
        ", weightStandard=" + weightStandard +
        ", firstWeightPrice=" + firstWeightPrice +
        ", firstWeight=" + firstWeight +
        ", price=" + price +
        ", firstOrContinued=" + firstOrContinued +
        "}";
    }
}
