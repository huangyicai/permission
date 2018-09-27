package com.mmall.model.params;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

@Getter
@Setter
public class TotalParam {

    @ApiModelProperty(value = "账单id")
    @NotBlank(message = "用户ID不能为空")
    private Integer totalId;

}
