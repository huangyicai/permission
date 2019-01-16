package com.mmall.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * <p>
 * 账单模板表
 * </p>
 *
 * @author qty
 * @since 2019-01-16
 */
@ApiModel(value = "TotalTemplate", description = "账单模板表")
@TableName("total_template")
@Builder
public class TotalTemplate implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "template_id", type = IdType.AUTO)
    private Integer templateId;
    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    @TableField("id")
    private Integer id;
    /**
     * 模板字段
     */
    @ApiModelProperty(value = "模板字段")
    @TableField("template_head")
    private String templateHead;
    private String templateNum;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @TableField("create_time")
    private Date createTime;


    public Integer getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Integer templateId) {
        this.templateId = templateId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTemplateHead() {
        return templateHead;
    }

    public void setTemplateHead(String templateHead) {
        this.templateHead = templateHead;
    }

    public String getTemplateNum() {
        return templateNum;
    }

    public void setTemplateNum(String templateNum) {
        this.templateNum = templateNum;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "TotalTemplate{" +
        ", templateId=" + templateId +
        ", id=" + id +
        ", templateHead=" + templateHead +
        ", templateNum=" + templateNum +
        ", createTime=" + createTime +
        "}";
    }
}
