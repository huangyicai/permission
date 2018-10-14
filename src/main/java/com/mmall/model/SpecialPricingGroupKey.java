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
@ApiModel(value = "SpecialPricingGroupKey", description = "")
@TableName("special_pricing_group_key")
@Builder
public class SpecialPricingGroupKey implements Serializable {

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
     * 特殊定价关键字
     */
    @ApiModelProperty(value = "特殊定价关键字")
    @TableField("key_name")
    private String keyName;


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

    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    public SpecialPricingGroupKey() {
    }

    public SpecialPricingGroupKey(Integer id, Integer userId, String keyName) {
        this.id = id;
        this.userId = userId;
        this.keyName = keyName;
    }

    @Override
    public String toString() {
        return "SpecialPricingGroupKey{" +
        ", id=" + id +
        ", userId=" + userId +
        ", keyName=" + keyName +
        "}";
    }
}
