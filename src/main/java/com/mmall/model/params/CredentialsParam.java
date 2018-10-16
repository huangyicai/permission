package com.mmall.model.params;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CredentialsParam {
    @ApiModelProperty(value = "账单id")
    private Integer totalId;

    @ApiModelProperty(value = "凭证id")
    private String totalCredentialsUrl;
}
