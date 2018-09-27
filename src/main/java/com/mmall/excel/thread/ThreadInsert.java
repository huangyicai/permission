package com.mmall.excel.thread;

import com.mmall.component.ApplicationContextHelper;
import com.mmall.dao.ProvinceCalculateMapper;
import com.mmall.dao.TotalMapper;
import com.mmall.dao.WeightCalculateMapper;
import com.mmall.model.ProvinceCalculate;
import com.mmall.model.Total;
import com.mmall.model.WeightCalculate;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.Callable;

@Transactional
public class ThreadInsert implements Callable<String> {

    private String key;//账单名
    private Map<Integer,BigDecimal> mw;//重量
    private Map<String,Integer> md;//省份
    private String time;//時間
    private Integer totalNum;//件數
    private BigDecimal weight;//重量
    private Integer id;//路徑

    public ThreadInsert(String key, Map<Integer,BigDecimal> mw, Map<String, Integer> md,String time,Integer totalNum,BigDecimal weight,Integer id) {
        this.key = key;
        this.mw = mw;
        this.md = md;
        this.time=time;
        this.totalNum=totalNum;
        this.weight=weight;
        this.id=id;
    }
    public ThreadInsert() {
    }
    /**
     * 线程执行导出
     * @return
     * @throws Exception
     */
    public String call() throws Exception {

        TotalMapper totalMapper = ApplicationContextHelper.getBeanClass(TotalMapper.class);
        WeightCalculateMapper weightCalculateMapper = ApplicationContextHelper.getBeanClass(WeightCalculateMapper.class);
        ProvinceCalculateMapper provinceCalculateMapper = ApplicationContextHelper.getBeanClass(ProvinceCalculateMapper.class);


        String path="E:/GDW/"+key+".xlsx";

        Total total=new Total();
        WeightCalculate weightCalculate=new WeightCalculate();
        ProvinceCalculate provinceCalculate=new ProvinceCalculate();

        //添加账单表数据
        total.setName(key);
        total.setUserId(id);
        total.setTotalTime(time);
        total.setTotalNumber(totalNum);
        total.setTotalWeight(weight);
        total.setTotalUrl(path);
        total.setCreateTime(new Date());
        totalMapper.insertTotal(total);

        //添加重量区间数据
        weightCalculate.setTotalId(total.getTotalId());
        weightCalculate.setZero(mw.get(0));
        weightCalculate.setOne(mw.get(1));
        weightCalculate.setTwo(mw.get(2));
        weightCalculate.setThree(mw.get(3));
        weightCalculate.setFour(mw.get(4));
        weightCalculate.setFive(mw.get(5));
        weightCalculate.setSix(mw.get(6));
        weightCalculate.setSeven(mw.get(7));
        weightCalculate.setEight(mw.get(8));
        weightCalculate.setNine(mw.get(9));
        weightCalculate.setTen(mw.get(10));
        weightCalculate.setEleven(mw.get(11));
        weightCalculate.setTwelve(mw.get(12));
        weightCalculate.setThirteen(mw.get(13));
        weightCalculate.setFourteen(mw.get(14));
        weightCalculate.setFifteen(mw.get(15));
        weightCalculate.setSixteen(mw.get(16));
        weightCalculate.setSeventeen(mw.get(17));
        weightCalculate.setEighteen(mw.get(18));
        weightCalculate.setNineteen(mw.get(19));
        weightCalculate.setTwenty(mw.get(20));
        weightCalculate.setTwentyOne(mw.get(21));
        weightCalculateMapper.insert(weightCalculate);

        //添加省计表数据
        provinceCalculate.setTotalId(total.getTotalId());
        provinceCalculate.setBeijing(md.get("北京"));
        provinceCalculate.setTianjing(md.get("天津"));
        provinceCalculate.setHebei(md.get("河北"));
        provinceCalculate.setShanxi(md.get("山西"));
        provinceCalculate.setNeimenggu(md.get("内蒙古"));
        provinceCalculate.setLiaoning(md.get("辽宁"));
        provinceCalculate.setJiling(md.get("吉林"));
        provinceCalculate.setHeilongjiang(md.get("黑龙江"));
        provinceCalculate.setShanghai(md.get("上海"));
        provinceCalculate.setJiangsu(md.get("江苏"));
        provinceCalculate.setZhejaing(md.get("浙江"));
        provinceCalculate.setAnhui(md.get("安徽"));
        provinceCalculate.setFujian(md.get("福建"));
        provinceCalculate.setJaingxi(md.get("江西"));
        provinceCalculate.setShandong(md.get("山东"));
        provinceCalculate.setHenan(md.get("河南"));
        provinceCalculate.setHubei(md.get("湖北"));
        provinceCalculate.setHunan(md.get("湖南"));
        provinceCalculate.setGuangdong(md.get("广东"));
        provinceCalculate.setGuangxi(md.get("广西"));
        provinceCalculate.setHainan(md.get("海南"));
        provinceCalculate.setChongqing(md.get("重庆"));
        provinceCalculate.setSichuan(md.get("四川"));
        provinceCalculate.setGuizhou(md.get("贵州"));
        provinceCalculate.setYunnan(md.get("云南"));
        provinceCalculate.setXizang(md.get("西藏"));
        provinceCalculate.setShanxi(md.get("陕西"));
        provinceCalculate.setGansu(md.get("甘肃"));
        provinceCalculate.setQinghai(md.get("青海"));
        provinceCalculate.setNingxia(md.get("宁夏"));
        provinceCalculate.setXinjang(md.get("新疆"));
        provinceCalculate.setTaiwan(md.get("台湾"));
        provinceCalculate.setXianggang(md.get("香港"));
        provinceCalculate.setAomen(md.get("澳门"));
        provinceCalculateMapper.insert(provinceCalculate);
        return "ok";
    }

}
