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
 * 重量区间计算表
 * </p>
 *
 * @author qty
 * @since 2018-09-25
 */
@ApiModel(value = "WeightCalculate", description = "重量区间计算表")
@TableName("weight_calculate")
@NoArgsConstructor
@AllArgsConstructor
public class WeightCalculate implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "weight_id", type = IdType.AUTO)
    private Integer weightId;
    /**
     * 月计表
     */
    @ApiModelProperty(value = "月计表")
    @TableField("total_id")
    private Integer totalId;
    /**
     * 其他
     */
    @ApiModelProperty(value = "其他")
    @TableField("zero")
    private BigDecimal zero;
    /**
     * 0.01-0.5
     */
    @ApiModelProperty(value = "0.01-0.5")
    @TableField("one")
    private BigDecimal one;
    /**
     * 0.5-1
     */
    @ApiModelProperty(value = "0.5-1")
    @TableField("two")
    private BigDecimal two;
    /**
     * 1-2
     */
    @ApiModelProperty(value = "1-2")
    @TableField("three")
    private BigDecimal three;
    /**
     * 2-3
     */
    @ApiModelProperty(value = "2-3")
    @TableField("four")
    private BigDecimal four;
    /**
     * 3-4
     */
    @ApiModelProperty(value = "3-4")
    @TableField("five")
    private BigDecimal five;
    /**
     * 4-5
     */
    @ApiModelProperty(value = "4-5")
    @TableField("six")
    private BigDecimal six;
    /**
     * 5-6
     */
    @ApiModelProperty(value = "5-6")
    @TableField("seven")
    private BigDecimal seven;
    /**
     * 6-7
     */
    @ApiModelProperty(value = "6-7")
    @TableField("eight")
    private BigDecimal eight;
    /**
     * 7-8
     */
    @ApiModelProperty(value = "7-8")
    @TableField("nine")
    private BigDecimal nine;
    /**
     * 8-9
     */
    @ApiModelProperty(value = "8-9")
    @TableField("ten")
    private BigDecimal ten;
    /**
     * 9-10
     */
    @ApiModelProperty(value = "9-10")
    @TableField("eleven")
    private BigDecimal eleven;
    /**
     * 10-11
     */
    @ApiModelProperty(value = "10-11")
    @TableField("twelve")
    private BigDecimal twelve;
    /**
     * 11-12
     */
    @ApiModelProperty(value = "11-12")
    @TableField("thirteen")
    private BigDecimal thirteen;
    /**
     * 12-13
     */
    @ApiModelProperty(value = "12-13")
    @TableField("fourteen")
    private BigDecimal fourteen;
    /**
     * 13-14
     */
    @ApiModelProperty(value = "13-14")
    @TableField("fifteen")
    private BigDecimal fifteen;
    /**
     * 14-15
     */
    @ApiModelProperty(value = "14-15")
    @TableField("sixteen")
    private BigDecimal sixteen;
    /**
     * 15-16
     */
    @ApiModelProperty(value = "15-16")
    @TableField("seventeen")
    private BigDecimal seventeen;
    /**
     * 16-17
     */
    @ApiModelProperty(value = "16-17")
    @TableField("eighteen")
    private BigDecimal eighteen;
    /**
     * 17-18
     */
    @ApiModelProperty(value = "17-18")
    @TableField("nineteen")
    private BigDecimal nineteen;
    /**
     * 18-19
     */
    @ApiModelProperty(value = "18-19")
    @TableField("twenty")
    private BigDecimal twenty;
    /**
     * 19-20
     */
    @ApiModelProperty(value = "19-20")
    @TableField("twenty_one")
    private BigDecimal twentyOne;


    public Integer getWeightId() {
        return weightId;
    }

    public void setWeightId(Integer weightId) {
        this.weightId = weightId;
    }

    public Integer getTotalId() {
        return totalId;
    }

    public void setTotalId(Integer totalId) {
        this.totalId = totalId;
    }

    public BigDecimal getZero() {
        return zero;
    }

    public void setZero(BigDecimal zero) {
        this.zero = zero;
    }

    public BigDecimal getOne() {
        return one;
    }

    public void setOne(BigDecimal one) {
        this.one = one;
    }

    public BigDecimal getTwo() {
        return two;
    }

    public void setTwo(BigDecimal two) {
        this.two = two;
    }

    public BigDecimal getThree() {
        return three;
    }

    public void setThree(BigDecimal three) {
        this.three = three;
    }

    public BigDecimal getFour() {
        return four;
    }

    public void setFour(BigDecimal four) {
        this.four = four;
    }

    public BigDecimal getFive() {
        return five;
    }

    public void setFive(BigDecimal five) {
        this.five = five;
    }

    public BigDecimal getSix() {
        return six;
    }

    public void setSix(BigDecimal six) {
        this.six = six;
    }

    public BigDecimal getSeven() {
        return seven;
    }

    public void setSeven(BigDecimal seven) {
        this.seven = seven;
    }

    public BigDecimal getEight() {
        return eight;
    }

    public void setEight(BigDecimal eight) {
        this.eight = eight;
    }

    public BigDecimal getNine() {
        return nine;
    }

    public void setNine(BigDecimal nine) {
        this.nine = nine;
    }

    public BigDecimal getTen() {
        return ten;
    }

    public void setTen(BigDecimal ten) {
        this.ten = ten;
    }

    public BigDecimal getEleven() {
        return eleven;
    }

    public void setEleven(BigDecimal eleven) {
        this.eleven = eleven;
    }

    public BigDecimal getTwelve() {
        return twelve;
    }

    public void setTwelve(BigDecimal twelve) {
        this.twelve = twelve;
    }

    public BigDecimal getThirteen() {
        return thirteen;
    }

    public void setThirteen(BigDecimal thirteen) {
        this.thirteen = thirteen;
    }

    public BigDecimal getFourteen() {
        return fourteen;
    }

    public void setFourteen(BigDecimal fourteen) {
        this.fourteen = fourteen;
    }

    public BigDecimal getFifteen() {
        return fifteen;
    }

    public void setFifteen(BigDecimal fifteen) {
        this.fifteen = fifteen;
    }

    public BigDecimal getSixteen() {
        return sixteen;
    }

    public void setSixteen(BigDecimal sixteen) {
        this.sixteen = sixteen;
    }

    public BigDecimal getSeventeen() {
        return seventeen;
    }

    public void setSeventeen(BigDecimal seventeen) {
        this.seventeen = seventeen;
    }

    public BigDecimal getEighteen() {
        return eighteen;
    }

    public void setEighteen(BigDecimal eighteen) {
        this.eighteen = eighteen;
    }

    public BigDecimal getNineteen() {
        return nineteen;
    }

    public void setNineteen(BigDecimal nineteen) {
        this.nineteen = nineteen;
    }

    public BigDecimal getTwenty() {
        return twenty;
    }

    public void setTwenty(BigDecimal twenty) {
        this.twenty = twenty;
    }

    public BigDecimal getTwentyOne() {
        return twentyOne;
    }

    public void setTwentyOne(BigDecimal twentyOne) {
        this.twentyOne = twentyOne;
    }

    @Override
    public String toString() {
        return "WeightCalculate{" +
        ", weightId=" + weightId +
        ", totalId=" + totalId +
        ", zero=" + zero +
        ", one=" + one +
        ", two=" + two +
        ", three=" + three +
        ", four=" + four +
        ", five=" + five +
        ", six=" + six +
        ", seven=" + seven +
        ", eight=" + eight +
        ", nine=" + nine +
        ", ten=" + ten +
        ", eleven=" + eleven +
        ", twelve=" + twelve +
        ", thirteen=" + thirteen +
        ", fourteen=" + fourteen +
        ", fifteen=" + fifteen +
        ", sixteen=" + sixteen +
        ", seventeen=" + seventeen +
        ", eighteen=" + eighteen +
        ", nineteen=" + nineteen +
        ", twenty=" + twenty +
        ", twentyOne=" + twentyOne +
        "}";
    }
}
