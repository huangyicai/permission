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
 * @since 2018-10-09
 */
@ApiModel(value = "CourierCompany", description = "")
@TableName("courier_company")
@Builder
public class CourierCompany implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    @TableField("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 颜色
     */
    @ApiModelProperty(value = "颜色")
    @TableField("color")
    private String color;

    @ApiModelProperty(value = "快递公司名称")
    @TableField("name")
    private String name;
    /**
     * logo地址
     */
    @ApiModelProperty(value = "logo地址")
    @TableField("logo_url")
    private String logoUrl;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    @Override
    public String toString() {
        return "CourierCompany{" +
        ", id=" + id +
        ", color=" + color +
        ", logoUrl=" + logoUrl +
        "}";
    }
}
