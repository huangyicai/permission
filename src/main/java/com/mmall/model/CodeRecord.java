package com.mmall.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.io.Serializable;


/**
 * 验证码
 * Created on 2018/3/21
 */
@ApiModel(value = "CodeRecord", description = "验证码")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName( "code_record")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CodeRecord  implements Serializable {

    @ApiModelProperty(value = "菜单id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "手机号码")
    private String phone;

    @ApiModelProperty(value = "验证码")
    private String code;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
