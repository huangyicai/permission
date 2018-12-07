package com.mmall.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class MessageVo implements Serializable {

    @ApiModelProperty(value = "信息id")
    private Integer id;

    @ApiModelProperty(value = "昵称")
    private String title;

    @ApiModelProperty(value = "内容")
    private String content;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "关联表id")
    private Integer userMessageId;
}
