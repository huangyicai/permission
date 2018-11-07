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
 * 弗恩快递负责人
 * </p>
 *
 * @author hyc
 * @since 2018-11-07
 */
@ApiModel(value = "FnContacts", description = "弗恩快递负责人")
@TableName("fn_contacts")
@Builder
public class FnContacts implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    @TableField("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 负责人
     */
    @ApiModelProperty(value = "负责人")
    @TableField("name")
    private String name;
    /**
     * 电话/手机
     */
    @ApiModelProperty(value = "电话/手机")
    @TableField("phone")
    private String phone;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "FnContacts{" +
        ", id=" + id +
        ", name=" + name +
        ", phone=" + phone +
        "}";
    }
}
