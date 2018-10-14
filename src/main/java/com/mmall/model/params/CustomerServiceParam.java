package com.mmall.model.params;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class CustomerServiceParam {

    @ApiModelProperty(value = "运单号")
    @NotBlank(message = "运单号不可以为空")
    private String waybillNumber;

    @ApiModelProperty(value = "问题描述")
    @NotBlank(message = "问题描述不可以为空")
    private String content;

    @ApiModelProperty(value = "联络人")
    @NotBlank(message = "运单号不可以为空")
    private String contacts;

    @ApiModelProperty(value = "联系电话")
    @NotBlank(message = "运单号不可以为空")
    private String phone;

    @ApiModelProperty(value = "附件地址")
    //@NotBlank(message = "运单号不可以为空")
    //@Pattern(regexp="^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[0,5-9]))\\d{8}$",message = "手机号不正确")
    private String enclosure;

    @ApiModelProperty(value = "时间段")
    private String timeSlot;

    @ApiModelProperty(value = "类型（1=破损，2=丢失，3=其他）")
    @NotNull(message = "其选择类型")
    private Integer typeId;

}
