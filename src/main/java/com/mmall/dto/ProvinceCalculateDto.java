package com.mmall.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class ProvinceCalculateDto implements Serializable {
    @ApiModelProperty(value = "省份")
    private String city;

    @ApiModelProperty(value = "单量")
    private Integer num;
}
