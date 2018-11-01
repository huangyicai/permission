package com.mmall.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.mmall.model.Response.Result;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;

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
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_menu")
@ApiModel(value = "")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysMenu extends Model<SysMenu> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "菜单id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    @JsonView(Result.ResultMenus.class)
    @ApiModelProperty(value = "图标")
    private String icon;
    @JsonView(Result.ResultMenus.class)
    @ApiModelProperty(value = "标题")
    private String title;
    @ApiModelProperty(value = "等级")
    private String level;
    @ApiModelProperty(value = "上级菜单ID")
    @TableField("parent_id")
    private Integer parentId;
    @JsonView(Result.ResultMenus.class)
    @ApiModelProperty(value = "路径")
    private String url;
    @ApiModelProperty(value = "该记录是否有效1：有效、0：无效")
    private Integer status;
    @ApiModelProperty(value = "顺序")
    private Integer seq;

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "SysMenu{" +
                ", id=" + id +
                ", icon=" + icon +
                ", title=" + title +
                ", level=" + level +
                ", parentId=" + parentId +
                ", url=" + url +
                ", status=" + status +
                "}";
    }
}
