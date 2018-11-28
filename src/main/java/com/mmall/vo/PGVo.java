package com.mmall.vo;

import com.mmall.model.PricingGroup;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PGVo extends PricingGroup {

    /**
     * 行数
     */
    private Integer row;

    /**
     * 省份是否无法识别
     */
    private boolean rowNo;

    /**
     * 是否是空
     */
    private boolean isNull;
}
