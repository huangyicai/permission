package com.mmall.model.params;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
@Getter
@Setter
public class PricingGroupParam {
    @NotNull(message = "必须指定开始区间")
    @ApiModelProperty(value = "开始区间")
    private Integer areaBegin;
    @NotNull(message = "必须指定结束区间")
    @ApiModelProperty(value = "结束区间")
    private Integer areaEnd;
    @NotNull(message = "必须指定重量标准")
    @ApiModelProperty(value = "重量标准")
    private Integer weightStandard;
    @NotNull(message = "必须指定价格")
    @ApiModelProperty(value = "价格")
    private Integer price;

    @NotNull(message = "必须指定首重还是续重")
    @Min(value = 0, message = "权限点状态不合法")
    @Max(value = 2, message = "权限点状态不合法（1=首重，2=续重）")
    @ApiModelProperty(value = "1=首重，2=续重")
    private Integer firstOrContinued;
}
