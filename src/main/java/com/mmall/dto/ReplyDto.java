package com.mmall.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Huang YiCai
 * @create 2018/11/09  14:30
 */
@Setter
@Getter
public class ReplyDto {
    @ApiModelProperty(value = "客服ID")
    private Integer user_id;
    @ApiModelProperty(value = "客服昵称")
    private String  name;
    @ApiModelProperty(value = "客服姓名")
    private String personInCharge;
    @ApiModelProperty(value = "区分账号")
    private Integer display;
    @ApiModelProperty(value = "总工单")
    private Integer totalallNum;
    @ApiModelProperty(value = "处理完毕工单数")
    private Integer handledNum;
    @ApiModelProperty(value = "处理中的工单数")
    private Integer handleingNum;
    @ApiModelProperty(value = "未处理工单数")
    private Integer noHandleNum;
}
