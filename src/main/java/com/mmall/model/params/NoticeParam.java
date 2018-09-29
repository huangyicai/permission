package com.mmall.model.params;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

@Getter
@Setter
public class NoticeParam {
    @ApiModelProperty(value = "内容")
    @NotBlank(message = "通知内容不可以为空")
    private String content;
    @ApiModelProperty(value = "标题")
    @NotBlank(message = "标题不可以为空")
    private String title;
}
