package com.mmall.model.params;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
@Setter
@Getter
public class UserPasswordParam {
    @ApiModelProperty(value = "密码")
    @NotBlank(message = "密码不可以为空")
    private String onePassword;

    @ApiModelProperty(value = "确认密码")
    @NotBlank(message = "确认密码不可以为空")
    private String twoPassword;
}
