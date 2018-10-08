package com.mmall.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;


/**
 * <p>
 * 
 * </p>
 *
 * @author hyc
 * @since 2018-09-28
 */
@ApiModel(value = "City", description = "省份")
@TableName("city")
@Builder
public class City implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    @TableField("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 省份名
     */
    @ApiModelProperty(value = "省份名")
    @TableField("province_name")
    private String provinceName;

    @TableField("province_key")
    private String provinceKey;




    public String getProvinceKey() {
        return provinceKey;
    }

    public void setProvinceKey(String provinceKey) {
        this.provinceKey = provinceKey;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public City() {
    }

    public City(Integer id, String provinceName, String provinceKey) {
        this.id = id;
        this.provinceName = provinceName;
        this.provinceKey = provinceKey;
    }

    @Override
    public String toString() {
        return "City{" +
        ", id=" + id +
        ", provinceName=" + provinceName +
        "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        City city = (City) o;
        return Objects.equals(id, city.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
