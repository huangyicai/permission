package com.mmall.model.params;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

@Setter
@Getter
public class UserInfoExpressParm  extends SysUserParam{


    private Integer id = 0;
    @ApiModelProperty(value = "昵称")
    @NotBlank(message = "昵称不可以为空")
    private String name;
    @ApiModelProperty(value = "用户邮箱")
    private String email;
    @ApiModelProperty(value = "公司名")
    @NotBlank(message = "公司名不可以为空")
    private String companyName;
    @ApiModelProperty(value = "地址：省")
    @NotBlank(message = "地址：省不可以为空")
    private String province;
    @ApiModelProperty(value = "地址：市")
    @NotBlank(message = "地址：市不可以为空")
    private String city;
    @ApiModelProperty(value = "地址：区")
    @NotBlank(message = "地址：区不可以为空")
    private String area;
    @ApiModelProperty(value = "详细地址")
    @NotBlank(message = "详细地址不可以为空")
    private String address;
    @ApiModelProperty(value = "联系电话/手机")
    @NotBlank(message = "昵联系电话不可以为空")
    private String telephone;
    @ApiModelProperty(value = "负责人")
    @NotBlank(message = "负责人不可以为空")
    private String personInCharge;
}
