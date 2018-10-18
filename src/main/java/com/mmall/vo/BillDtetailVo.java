package com.mmall.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.mmall.model.Total;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author Huang YiCai
 * @create 2018/10/16  18:12
 */
@Getter
@Setter
public class BillDtetailVo extends Total {
    @ApiModelProperty(value = "昵称")
    private String nameNick;

    @ApiModelProperty(value = "负责人")
    @TableField("person_in_charge")
    private String personInCharge;
}
