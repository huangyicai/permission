package com.mmall.model.params;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @author Huang YiCai
 * @create 2018/10/22  11:46
 */
@Setter
@Getter
public class WorkReplyParam {

    @ApiModelProperty(value = "回复内容")
    @NotBlank(message = "回复内容不可以为空")
    private String content;
}
