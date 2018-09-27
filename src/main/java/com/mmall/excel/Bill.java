package com.mmall.excel;

import java.math.BigDecimal;

public class Bill {
    /**
     * 商家名称
     */
    private String billName;

    /**
     * 扫描时间
     */
    private String sweepTime;

    /**
     * 运单编号
     */
    private String serialNumber;

    /**
     * 目的地
     */
    private String destination;

    /**
     * 快递重量
     */
    private BigDecimal weight;

    /**
     * 运费
     */
    private BigDecimal freight;

    public String getBillName() {
        return billName;
    }

    public void setBillName(String billName) {
        this.billName = billName;
    }

    public String getSweepTime() {
        return sweepTime;
    }

    public void setSweepTime(String sweepTime) {
        this.sweepTime = sweepTime;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public BigDecimal getFreight() {
        return freight;
    }

    public void setFreight(BigDecimal freight) {
        this.freight = freight;
    }

    public Bill(String billName, String sweepTime, String serialNumber, String destination, BigDecimal weight, BigDecimal freight) {
        this.billName = billName;
        this.sweepTime = sweepTime;
        this.serialNumber = serialNumber;
        this.destination = destination;
        this.weight = weight;
        this.freight = freight;
    }

    public Bill(String billName, String sweepTime, String serialNumber, String destination, BigDecimal weight) {
        this.billName = billName;
        this.sweepTime = sweepTime;
        this.serialNumber = serialNumber;
        this.destination = destination;
        this.weight = weight;
    }

    public Bill() {
    }
}
