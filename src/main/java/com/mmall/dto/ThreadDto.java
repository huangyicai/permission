package com.mmall.dto;

import com.mmall.excel.Bill;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

//@Getter
//@Setter
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
    private String md;

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

    /**
     * 总账单名字
     */
    private String name;

    /**
     * 总账单id
     */
    private Integer sumId;

    /**
     * 公司名
     */
    private String companyName;

    /**
     * 每日单量
     */
    private String daily;

    /**
     * 每日单量的时间
     */
    private String dailyTime;

    private BigDecimal cost;

    private BigDecimal off;

    public BigDecimal getOff() {
        return off;
    }

    public void setOff(BigDecimal off) {
        this.off = off;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Map<Integer, BigDecimal> getMw() {
        return mw;
    }

    public void setMw(Map<Integer, BigDecimal> mw) {
        this.mw = mw;
    }

    public String getMd() {
        return md;
    }

    public void setMd(String md) {
        this.md = md;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(Integer totalNum) {
        this.totalNum = totalNum;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSendId() {
        return sendId;
    }

    public void setSendId(Integer sendId) {
        this.sendId = sendId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<Bill> getList() {
        return list;
    }

    public void setList(List<Bill> list) {
        this.list = list;
    }

    public String getPathHead() {
        return pathHead;
    }

    public void setPathHead(String pathHead) {
        this.pathHead = pathHead;
    }

    public String getIdtime() {
        return Idtime;
    }

    public void setIdtime(String idtime) {
        Idtime = idtime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSumId() {
        return sumId;
    }

    public void setSumId(Integer sumId) {
        this.sumId = sumId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getDaily() {
        return daily;
    }

    public void setDaily(String daily) {
        this.daily = daily;
    }

    public String getDailyTime() {
        return dailyTime;
    }

    public void setDailyTime(String dailyTime) {
        this.dailyTime = dailyTime;
    }
}
