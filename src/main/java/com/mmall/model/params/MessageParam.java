package com.mmall.model.params;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

@Getter
@Setter
public class MessageParam implements Serializable {

    @ApiModelProperty(value = "接收者id（逗号隔开，末尾不加逗号）")
    @NotBlank(message = "接收者不能为空")
    private String userId;

    @ApiModelProperty(value = "标题")
    @NotBlank(message = "标题不能为空")
    private String title;

    @ApiModelProperty(value = "内容")
    @NotBlank(message = "内容不能为空")
    private String content;
}
