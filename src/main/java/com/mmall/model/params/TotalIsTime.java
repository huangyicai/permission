package com.mmall.model.params;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class TotalIsTime implements Serializable {

    @ApiModelProperty(value = "时间")
    private String time;
}
