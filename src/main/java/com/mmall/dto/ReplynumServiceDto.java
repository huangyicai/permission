package com.mmall.dto;

import com.mmall.model.CustomerService;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.BeanUtils;

/**
 * @author Huang YiCai
 * @create 2018/11/13  11:41
 */
@Getter
@Setter
@ToString
public class ReplynumServiceDto extends CustomerService {
    /**
     * 未读回复数
     */
    @ApiModelProperty(value = "未读回复数")
    private Integer replyNum=0;


    public static ReplynumServiceDto adapt(CustomerService customerService) {
        ReplynumServiceDto dto = new ReplynumServiceDto();
        BeanUtils.copyProperties(customerService, dto);
        return dto;
    }
}
