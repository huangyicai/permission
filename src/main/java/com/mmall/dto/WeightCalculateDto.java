package com.mmall.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class WeightCalculateDto implements Serializable {
    @ApiModelProperty(value = "区间")
    private String interval;

    @ApiModelProperty(value = "重量")
    private Double weight;
}
