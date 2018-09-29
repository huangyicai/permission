package com.mmall.model.params;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
@Setter
@Getter
public class UserInfoOperateParam extends SysUserParam{
    @ApiModelProperty(value = "昵称")
    @NotBlank(message = "昵称不可以为空")
    private String name;
    @ApiModelProperty(value = "用户邮箱")
    private String email;
    @ApiModelProperty(value = "联系电话/手机")
    @NotBlank(message = "昵联系电话不可以为空")
    private String telephone;
    @ApiModelProperty(value = "负责人")
    @NotBlank(message = "负责人不可以为空")
    private String personInCharge;
}
