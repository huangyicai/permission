package com.mmall.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.io.Serializable;


/**
 * <p>
 * 省计表
 * </p>
 *
 * @author qty
 * @since 2018-09-25
 */
@ApiModel(value = "ProvinceCalculate", description = "省计表")
@TableName("province_calculate")
@NoArgsConstructor
@AllArgsConstructor
public class ProvinceCalculate implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "province_id", type = IdType.AUTO)
    private Integer provinceId;
    /**
     * 月计表
     */
    @ApiModelProperty(value = "月计表")
    @TableField("total_id")
    private Integer totalId;
    private Integer beijing;
    private Integer tianjing;
    private Integer hebei;
    private Integer shanxi;
    private Integer neimenggu;
    private Integer liaoning;
    private Integer jiling;
    private Integer heilongjiang;
    private Integer shanghai;
    private Integer jiangsu;
    private Integer zhejaing;
    private Integer anhui;
    private Integer fujian;
    private Integer jaingxi;
    private Integer shandong;
    private Integer henan;
    private Integer hubei;
    private Integer hunan;
    private Integer guangdong;
    private Integer guangxi;
    private Integer hainan;
    private Integer chongqing;
    private Integer sichuan;
    private Integer guizhou;
    private Integer yunnan;
    private Integer xizang;
    private Integer shaanxi;
    private Integer gansu;
    private Integer qinghai;
    private Integer ningxia;
    private Integer xinjang;
    private Integer taiwan;
    private Integer xianggang;
    private Integer aomen;


    public Integer getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Integer provinceId) {
        this.provinceId = provinceId;
    }

    public Integer getTotalId() {
        return totalId;
    }

    public void setTotalId(Integer totalId) {
        this.totalId = totalId;
    }

    public Integer getBeijing() {
        return beijing;
    }

    public void setBeijing(Integer beijing) {
        this.beijing = beijing;
    }

    public Integer getTianjing() {
        return tianjing;
    }

    public void setTianjing(Integer tianjing) {
        this.tianjing = tianjing;
    }

    public Integer getHebei() {
        return hebei;
    }

    public void setHebei(Integer hebei) {
        this.hebei = hebei;
    }

    public Integer getShanxi() {
        return shanxi;
    }

    public void setShanxi(Integer shanxi) {
        this.shanxi = shanxi;
    }

    public Integer getNeimenggu() {
        return neimenggu;
    }

    public void setNeimenggu(Integer neimenggu) {
        this.neimenggu = neimenggu;
    }

    public Integer getLiaoning() {
        return liaoning;
    }

    public void setLiaoning(Integer liaoning) {
        this.liaoning = liaoning;
    }

    public Integer getJiling() {
        return jiling;
    }

    public void setJiling(Integer jiling) {
        this.jiling = jiling;
    }

    public Integer getHeilongjiang() {
        return heilongjiang;
    }

    public void setHeilongjiang(Integer heilongjiang) {
        this.heilongjiang = heilongjiang;
    }

    public Integer getShanghai() {
        return shanghai;
    }

    public void setShanghai(Integer shanghai) {
        this.shanghai = shanghai;
    }

    public Integer getJiangsu() {
        return jiangsu;
    }

    public void setJiangsu(Integer jiangsu) {
        this.jiangsu = jiangsu;
    }

    public Integer getZhejaing() {
        return zhejaing;
    }

    public void setZhejaing(Integer zhejaing) {
        this.zhejaing = zhejaing;
    }

    public Integer getAnhui() {
        return anhui;
    }

    public void setAnhui(Integer anhui) {
        this.anhui = anhui;
    }

    public Integer getFujian() {
        return fujian;
    }

    public void setFujian(Integer fujian) {
        this.fujian = fujian;
    }

    public Integer getJaingxi() {
        return jaingxi;
    }

    public void setJaingxi(Integer jaingxi) {
        this.jaingxi = jaingxi;
    }

    public Integer getShandong() {
        return shandong;
    }

    public void setShandong(Integer shandong) {
        this.shandong = shandong;
    }

    public Integer getHenan() {
        return henan;
    }

    public void setHenan(Integer henan) {
        this.henan = henan;
    }

    public Integer getHubei() {
        return hubei;
    }

    public void setHubei(Integer hubei) {
        this.hubei = hubei;
    }

    public Integer getHunan() {
        return hunan;
    }

    public void setHunan(Integer hunan) {
        this.hunan = hunan;
    }

    public Integer getGuangdong() {
        return guangdong;
    }

    public void setGuangdong(Integer guangdong) {
        this.guangdong = guangdong;
    }

    public Integer getGuangxi() {
        return guangxi;
    }

    public void setGuangxi(Integer guangxi) {
        this.guangxi = guangxi;
    }

    public Integer getHainan() {
        return hainan;
    }

    public void setHainan(Integer hainan) {
        this.hainan = hainan;
    }

    public Integer getChongqing() {
        return chongqing;
    }

    public void setChongqing(Integer chongqing) {
        this.chongqing = chongqing;
    }

    public Integer getSichuan() {
        return sichuan;
    }

    public void setSichuan(Integer sichuan) {
        this.sichuan = sichuan;
    }

    public Integer getGuizhou() {
        return guizhou;
    }

    public void setGuizhou(Integer guizhou) {
        this.guizhou = guizhou;
    }

    public Integer getYunnan() {
        return yunnan;
    }

    public void setYunnan(Integer yunnan) {
        this.yunnan = yunnan;
    }

    public Integer getXizang() {
        return xizang;
    }

    public void setXizang(Integer xizang) {
        this.xizang = xizang;
    }

    public Integer getShaanxi() {
        return shaanxi;
    }

    public void setShaanxi(Integer shaanxi) {
        this.shaanxi = shaanxi;
    }

    public Integer getGansu() {
        return gansu;
    }

    public void setGansu(Integer gansu) {
        this.gansu = gansu;
    }

    public Integer getQinghai() {
        return qinghai;
    }

    public void setQinghai(Integer qinghai) {
        this.qinghai = qinghai;
    }

    public Integer getNingxia() {
        return ningxia;
    }

    public void setNingxia(Integer ningxia) {
        this.ningxia = ningxia;
    }

    public Integer getXinjang() {
        return xinjang;
    }

    public void setXinjang(Integer xinjang) {
        this.xinjang = xinjang;
    }

    public Integer getTaiwan() {
        return taiwan;
    }

    public void setTaiwan(Integer taiwan) {
        this.taiwan = taiwan;
    }

    public Integer getXianggang() {
        return xianggang;
    }

    public void setXianggang(Integer xianggang) {
        this.xianggang = xianggang;
    }

    public Integer getAomen() {
        return aomen;
    }

    public void setAomen(Integer aomen) {
        this.aomen = aomen;
    }

    @Override
    public String toString() {
        return "ProvinceCalculate{" +
        ", provinceId=" + provinceId +
        ", totalId=" + totalId +
        ", beijing=" + beijing +
        ", tianjing=" + tianjing +
        ", hebei=" + hebei +
        ", shanxi=" + shanxi +
        ", neimenggu=" + neimenggu +
        ", liaoning=" + liaoning +
        ", jiling=" + jiling +
        ", heilongjiang=" + heilongjiang +
        ", shanghai=" + shanghai +
        ", jiangsu=" + jiangsu +
        ", zhejaing=" + zhejaing +
        ", anhui=" + anhui +
        ", fujian=" + fujian +
        ", jaingxi=" + jaingxi +
        ", shandong=" + shandong +
        ", henan=" + henan +
        ", hubei=" + hubei +
        ", hunan=" + hunan +
        ", guangdong=" + guangdong +
        ", guangxi=" + guangxi +
        ", hainan=" + hainan +
        ", chongqing=" + chongqing +
        ", sichuan=" + sichuan +
        ", guizhou=" + guizhou +
        ", yunnan=" + yunnan +
        ", xizang=" + xizang +
        ", shaanxi=" + shaanxi +
        ", gansu=" + gansu +
        ", qinghai=" + qinghai +
        ", ningxia=" + ningxia +
        ", xinjang=" + xinjang +
        ", taiwan=" + taiwan +
        ", xianggang=" + xianggang +
        ", aomen=" + aomen +
        "}";
    }
}
