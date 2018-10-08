package com.mmall.dto;

import com.mmall.excel.Bill;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class ThreadDto {
    /**
     * 账单名
     */
    private String key;

    /**
     * 重量区间信息
     */
    private Map<Integer,BigDecimal> mw;

    /**
     * 省份区间信息
     */
    private Map<String,Integer> md;

    /**
     * 時間
     */
    private String time;

    /**
     * 件數
     */
    private Integer totalNum;

    /**
     * 重量
     */
    private BigDecimal weight;

    /**
     * 用户详情id
     */
    private Integer id;

    /**
     * 用户详情id
     */
    private Integer sendId;

    /**
     * excel路径
     */
    private String path;

    /**
     * 导出的集合
     */
    private List<Bill> list;

    /**
     * 本地地址头
     */
    private String pathHead;

    /**
     * 主键生成的时间
     */
    private String Idtime;
}
