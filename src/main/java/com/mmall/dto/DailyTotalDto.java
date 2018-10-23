package com.mmall.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class DailyTotalDto implements Serializable {

    @ApiModelProperty(value = "天")
    private String day;

    @ApiModelProperty(value = "单数")
    private Integer num=0;
}
