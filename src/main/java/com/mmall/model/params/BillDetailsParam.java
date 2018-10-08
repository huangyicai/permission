package com.mmall.model.params;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

@Getter
@Setter
public class BillDetailsParam {

    @ApiModelProperty(value = "当前页")
    @NotBlank(message = "当前页不能为空")
    private Integer current;

    @ApiModelProperty(value = "当前页大小")
    @NotBlank(message = "当前页大小不能为空")
    private Integer size;

    @ApiModelProperty(value = "时间")
    @NotBlank(message = "时间不能为空")
    private String date;

    @ApiModelProperty(value = "状态：1-未发送，2-待确认，3-已付款（未确认，这里不做展示），4-已收款")
    @NotBlank(message = "状态不能为空")
    private Integer state;

    @ApiModelProperty(value = "用户id（数据分析时不填，上传账单页面填用户id）")
    private Integer userId;
}
