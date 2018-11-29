package com.mmall.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.mmall.dto.SysUserInfoDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;

import java.io.Serializable;


/**
 * <p>
 *
 * </p>
 *
 * @author hyc
 * @since 2018-09-20
 */
@Builder
@TableName("sys_user_info")
@ApiModel(value = "")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysUserInfo extends Model<SysUserInfo> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    @JsonView(SysUserInfoDto.UserInfoView.class)
    private Integer id;
    @ApiModelProperty(value = "用户id")
    @TableField("user_id")
    private Integer userId;
    @ApiModelProperty(value = "昵称")
    @JsonView(SysUserInfoDto.UserInfoView.class)
    private String name;
    @ApiModelProperty(value = "用户邮箱")
    private String email;
    @ApiModelProperty(value = "公司名")
    @TableField("company_name")
    private String companyName;
    @ApiModelProperty(value = "地址：省")
    private String province;
    @ApiModelProperty(value = "地址：市")
    private String city;
    @ApiModelProperty(value = "地址：区")
    private String area;
    @ApiModelProperty(value = "详细地址")
    private String address;
    @ApiModelProperty(value = "联系电话/手机")
    private String telephone;
    @ApiModelProperty(value = "负责人")
    @JsonView(SysUserInfoDto.UserInfoView.class)
    @TableField("person_in_charge")
    private String personInCharge;

    @ApiModelProperty(value = "该条记录是否有效1:有效、0：无效 ,-1冻结")
    private Integer status;

    @ApiModelProperty(value = "是否自动定价成本：0-否，1-是")
    @TableField("pricing_status")
    private Integer pricingStatus;

    @ApiModelProperty(value = "2：运营好 ，1:老板号，0：其他")
    private Integer display;

    @ApiModelProperty(value = "创建时间")
    @TableField("create_time")
    private String createTime;
    @ApiModelProperty(value = "更新时间")
    @TableField("update_time")
    private String updateTime;

    @ApiModelProperty(value = "等级")
    private String level;
    @ApiModelProperty(value = "上级用户")
    @TableField("parent_id")
    private Integer parentId;

    @ApiModelProperty(value = "快递公司ID")
    @TableField("courier_id")
    private Integer courierId;

    @ApiModelProperty(value = "-1=快递分支 1:弗恩平台 2:快递平台 3:客户平台")
    @TableField("platform_id")
    @JsonView(SysUserInfoDto.UserInfoView.class)
    private Integer platformId;
    //private String  platformName;


    public Integer getPricingStatus() {
        return pricingStatus;
    }

    public void setPricingStatus(Integer pricingStatus) {
        this.pricingStatus = pricingStatus;
    }

    public Integer getCourierId() {
        return courierId;
    }

    public void setCourierId(Integer courierId) {
        this.courierId = courierId;
    }

    public Integer getDisplay() {
        return display;
    }

    public void setDisplay(Integer display) {
        this.display = display;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getPersonInCharge() {
        return personInCharge;
    }

    public void setPersonInCharge(String personInCharge) {
        this.personInCharge = personInCharge;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getPlatformId() {
        return platformId;
    }

    public void setPlatformId(Integer platformId) {
        this.platformId = platformId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

  /*  public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }
*/
    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public SysUserInfo() {
    }

    public SysUserInfo(Integer id, Integer userId, String name, String email, String companyName, String province, String city, String area, String address, String telephone, String personInCharge, Integer status, Integer display, String createTime, String updateTime, String level, Integer parentId, Integer courierId, Integer platformId) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.companyName = companyName;
        this.province = province;
        this.city = city;
        this.area = area;
        this.address = address;
        this.telephone = telephone;
        this.personInCharge = personInCharge;
        this.status = status;
        this.display = display;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.level = level;
        this.parentId = parentId;
        this.courierId = courierId;
        this.platformId = platformId;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "SysUserInfo{" +
                ", id=" + id +
                ", userId=" + userId +
                ", platformId=" + platformId +
                ", name=" + name +
                ", email=" + email +
                ", status=" + status +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SysUserInfo that = (SysUserInfo) o;

        if (!id.equals(that.id)) return false;
        if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (email != null ? !email.equals(that.email) : that.email != null) return false;
        if (companyName != null ? !companyName.equals(that.companyName) : that.companyName != null) return false;
        if (province != null ? !province.equals(that.province) : that.province != null) return false;
        if (city != null ? !city.equals(that.city) : that.city != null) return false;
        if (area != null ? !area.equals(that.area) : that.area != null) return false;
        if (address != null ? !address.equals(that.address) : that.address != null) return false;
        if (telephone != null ? !telephone.equals(that.telephone) : that.telephone != null) return false;
        if (personInCharge != null ? !personInCharge.equals(that.personInCharge) : that.personInCharge != null)
            return false;
        if (status != null ? !status.equals(that.status) : that.status != null) return false;
        if (display != null ? !display.equals(that.display) : that.display != null) return false;
        if (createTime != null ? !createTime.equals(that.createTime) : that.createTime != null) return false;
        if (updateTime != null ? !updateTime.equals(that.updateTime) : that.updateTime != null) return false;
        if (level != null ? !level.equals(that.level) : that.level != null) return false;
        if (parentId != null ? !parentId.equals(that.parentId) : that.parentId != null) return false;
        if (courierId != null ? !courierId.equals(that.courierId) : that.courierId != null) return false;
        return platformId != null ? platformId.equals(that.platformId) : that.platformId == null;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (companyName != null ? companyName.hashCode() : 0);
        result = 31 * result + (province != null ? province.hashCode() : 0);
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + (area != null ? area.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (telephone != null ? telephone.hashCode() : 0);
        result = 31 * result + (personInCharge != null ? personInCharge.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (display != null ? display.hashCode() : 0);
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        result = 31 * result + (updateTime != null ? updateTime.hashCode() : 0);
        result = 31 * result + (level != null ? level.hashCode() : 0);
        result = 31 * result + (parentId != null ? parentId.hashCode() : 0);
        result = 31 * result + (courierId != null ? courierId.hashCode() : 0);
        result = 31 * result + (platformId != null ? platformId.hashCode() : 0);
        return result;
    }

    public SysUserInfo(Integer id, Integer userId, String name, String email, String companyName, String province, String city, String area, String address, String telephone, String personInCharge, Integer status, Integer pricingStatus, Integer display, String createTime, String updateTime, String level, Integer parentId, Integer courierId, Integer platformId) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.companyName = companyName;
        this.province = province;
        this.city = city;
        this.area = area;
        this.address = address;
        this.telephone = telephone;
        this.personInCharge = personInCharge;
        this.status = status;
        this.pricingStatus = pricingStatus;
        this.display = display;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.level = level;
        this.parentId = parentId;
        this.courierId = courierId;
        this.platformId = platformId;
    }
}
