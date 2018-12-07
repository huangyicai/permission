package com.mmall.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class userMessageVo extends MessageVo {

    @ApiModelProperty(value = "用户名")
    private String name;

    @ApiModelProperty(value = "0-未读，1-已读")
    private Integer state;
}
