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
import java.math.BigDecimal;


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
        if(beijing==null||beijing==0){
            this.beijing =0;
        }else {
            this.beijing = beijing;
        }
    }

    public Integer getTianjing() {
        return tianjing;
    }

    public void setTianjing(Integer tianjing) {
        if(tianjing==null||tianjing==0){
            this.tianjing =0;
        }else {
            this.tianjing = tianjing;
        }
    }

    public Integer getHebei() {
        return hebei;
    }

    public void setHebei(Integer hebei) {
        if(hebei==null||hebei==0){
            this.hebei =0;
        }else {
            this.hebei = hebei;
        }
    }

    public Integer getShanxi() {
        return shanxi;
    }

    public void setShanxi(Integer shanxi) {
        if(shanxi==null||shanxi==0){
            this.shanxi =0;
        }else {
            this.shanxi = shanxi;
        }
    }

    public Integer getNeimenggu() {
        return neimenggu;
    }

    public void setNeimenggu(Integer neimenggu) {
        if(neimenggu==null||neimenggu==0){
            this.neimenggu =0;
        }else {
            this.neimenggu = neimenggu;
        }
    }

    public Integer getLiaoning() {
        return liaoning;
    }

    public void setLiaoning(Integer liaoning) {
        if(liaoning==null||liaoning==0){
            this.liaoning =0;
        }else {
            this.liaoning = liaoning;
        }
    }

    public Integer getJiling() {
        return jiling;
    }

    public void setJiling(Integer jiling) {
        if(jiling==null||jiling==0){
            this.jiling =0;
        }else {
            this.jiling = jiling;
        }
    }

    public Integer getHeilongjiang() {
        return heilongjiang;
    }

    public void setHeilongjiang(Integer heilongjiang) {
        if(heilongjiang==null||heilongjiang==0){
            this.heilongjiang =0;
        }else {
            this.heilongjiang = heilongjiang;
        }
    }

    public Integer getShanghai() {
        return shanghai;
    }

    public void setShanghai(Integer shanghai) {
        if(shanghai==null||shanghai==0){
            this.shanghai =0;
        }else {
            this.shanghai = shanghai;
        }
    }

    public Integer getJiangsu() {
        return jiangsu;
    }

    public void setJiangsu(Integer jiangsu) {
        if(jiangsu==null||jiangsu==0){
            this.jiangsu =0;
        }else {
            this.jiangsu = jiangsu;
        }
    }

    public Integer getZhejaing() {
        return zhejaing;
    }

    public void setZhejaing(Integer zhejaing) {
        if(zhejaing==null||zhejaing==0){
            this.zhejaing =0;
        }else {
            this.zhejaing = zhejaing;
        }
    }

    public Integer getAnhui() {
        return anhui;
    }

    public void setAnhui(Integer anhui) {
        if(anhui==null||anhui==0){
            this.anhui =0;
        }else {
            this.anhui = anhui;
        }
    }

    public Integer getFujian() {
        return fujian;
    }

    public void setFujian(Integer fujian) {
        if(fujian==null||fujian==0){
            this.fujian =0;
        }else {
            this.fujian = fujian;
        }
    }

    public Integer getJaingxi() {
        return jaingxi;
    }

    public void setJaingxi(Integer jaingxi) {
        if(jaingxi==null||jaingxi==0){
            this.jaingxi =0;
        }else {
            this.jaingxi = jaingxi;
        }
    }

    public Integer getShandong() {
        return shandong;
    }

    public void setShandong(Integer shandong) {
        if(shandong==null||shandong==0){
            this.shandong =0;
        }else {
            this.shandong = shandong;
        }
    }

    public Integer getHenan() {
        return henan;
    }

    public void setHenan(Integer henan) {
        if(henan==null||henan==0){
            this.henan =0;
        }else {
            this.henan = henan;
        }
    }

    public Integer getHubei() {
        return hubei;
    }

    public void setHubei(Integer hubei) {
        if(hubei==null||hubei==0){
            this.hubei =0;
        }else {
            this.hubei = hubei;
        }
    }

    public Integer getHunan() {
        return hunan;
    }

    public void setHunan(Integer hunan) {
        if(hunan==null||hunan==0){
            this.hunan =0;
        }else {
            this.hunan = hunan;
        }
    }

    public Integer getGuangdong() {
        return guangdong;
    }

    public void setGuangdong(Integer guangdong) {
        if(guangdong==null||guangdong==0){
            this.guangdong =0;
        }else {
            this.guangdong = guangdong;
        }
    }

    public Integer getGuangxi() {
        return guangxi;
    }

    public void setGuangxi(Integer guangxi) {
        if(guangxi==null||guangxi==0){
            this.guangxi =0;
        }else {
            this.guangxi = guangxi;
        }
    }

    public Integer getHainan() {
        return hainan;
    }

    public void setHainan(Integer hainan) {
        if(hainan==null||hainan==0){
            this.hainan =0;
        }else {
            this.hainan = hainan;
        }
    }

    public Integer getChongqing() {
        return chongqing;
    }

    public void setChongqing(Integer chongqing) {
        if(chongqing==null||chongqing==0){
            this.chongqing =0;
        }else {
            this.chongqing = chongqing;
        }
    }

    public Integer getSichuan() {
        return sichuan;
    }

    public void setSichuan(Integer sichuan) {
        if(sichuan==null||sichuan==0){
            this.sichuan =0;
        }else {
            this.sichuan = sichuan;
        }
    }

    public Integer getGuizhou() {
        return guizhou;
    }

    public void setGuizhou(Integer guizhou) {
        if(guizhou==null||guizhou==0){
            this.guizhou =0;
        }else {
            this.guizhou = guizhou;
        }
    }

    public Integer getYunnan() {
        return yunnan;
    }

    public void setYunnan(Integer yunnan) {
        if(yunnan==null||yunnan==0){
            this.yunnan =0;
        }else {
            this.yunnan = yunnan;
        }
    }

    public Integer getXizang() {
        return xizang;
    }

    public void setXizang(Integer xizang) {
        if(xizang==null||xizang==0){
            this.xizang =0;
        }else {
            this.xizang = xizang;
        }
    }

    public Integer getShaanxi() {
        return shaanxi;
    }

    public void setShaanxi(Integer shaanxi) {
        if(shaanxi==null||shaanxi==0){
            this.shaanxi =0;
        }else {
            this.shaanxi = shaanxi;
        }
    }

    public Integer getGansu() {
        return gansu;
    }

    public void setGansu(Integer gansu) {
        if(gansu==null||gansu==0){
            this.gansu =0;
        }else {
            this.gansu = gansu;
        }
    }

    public Integer getQinghai() {
        return qinghai;
    }

    public void setQinghai(Integer qinghai) {
        if(qinghai==null||qinghai==0){
            this.qinghai =0;
        }else {
            this.qinghai = qinghai;
        }
    }

    public Integer getNingxia() {
        return ningxia;
    }

    public void setNingxia(Integer ningxia) {
        if(ningxia==null||ningxia==0){
            this.ningxia =0;
        }else {
            this.ningxia = ningxia;
        }
    }

    public Integer getXinjang() {
        return xinjang;
    }

    public void setXinjang(Integer xinjang) {
        if(xinjang==null||xinjang==0){
            this.xinjang =0;
        }else {
            this.xinjang = xinjang;
        }
    }

    public Integer getTaiwan() {
        return taiwan;
    }

    public void setTaiwan(Integer taiwan) {
        if(taiwan==null||taiwan==0){
            this.taiwan =0;
        }else {
            this.taiwan = taiwan;
        }
    }

    public Integer getXianggang() {
        return xianggang;
    }

    public void setXianggang(Integer xianggang) {
        if(xianggang==null||xianggang==0){
            this.xianggang =0;
        }else {
            this.xianggang = xianggang;
        }
    }

    public Integer getAomen() {
        return aomen;
    }

    public void setAomen(Integer aomen) {
        if(aomen==null||aomen==0){
            this.aomen =0;
        }else {
            this.aomen = aomen;
        }
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
