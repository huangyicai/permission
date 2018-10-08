package com.mmall.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class PricingGroupVo implements Serializable {

    /**
     * id
     */
    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 省份
     */
    private String city;

    /**
     * 开始区间
     */

    private Double areaBegin;

    /**
     * 结束区间
     */
    private Double areaEnd;

    /**
     * 重量标准
     */
    private Double weightStandard;

    /**
     * 价格
     */
    private Double price;

    /**
     * 1=首重，2=续重
     */
    private Integer firstOrContinued;
}
