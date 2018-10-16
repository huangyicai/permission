package com.mmall.model.params;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @author Huang YiCai
 * @create 2018/10/15  13:05
 */
@Getter
@Setter
public class keyNameParam {
    @ApiModelProperty(value = "关键字")
    private String keyName;
}
