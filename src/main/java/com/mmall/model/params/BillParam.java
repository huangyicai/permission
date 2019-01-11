package com.mmall.model.params;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

@Getter
@Setter
public class BillParam {

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "时间")
    @NotBlank(message = "时间不可以为空")
    private String date;

    @ApiModelProperty(value = "结束时间")
    private String endDate;
}
