package com.mmall.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class UserMessageVo implements Serializable {

    @ApiModelProperty(value = "用户名")
    private String name;

    @ApiModelProperty(value = "0-未读，1-已读")
    private Integer state;
}
