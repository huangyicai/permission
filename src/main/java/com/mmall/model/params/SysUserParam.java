package com.mmall.model.params;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

@Getter
@Setter
public class SysUserParam {

    @ApiModelProperty(value = "用户名")
    @NotBlank(message = "用户名不可以为空")
    private String username;

    @ApiModelProperty(value = "密码")
    @NotBlank(message = "密码不可以为空")
    private String password;
}
