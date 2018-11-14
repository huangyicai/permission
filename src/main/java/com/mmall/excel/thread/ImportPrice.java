package com.mmall.excel.thread;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.mmall.component.ApplicationContextHelper;
import com.mmall.dao.PricingGroupMapper;
import com.mmall.model.PricingGroup;

import java.util.List;


/**
 * 导入定价组
 */
public class ImportPrice extends Thread {

    private PricingGroup pg;

    public ImportPrice(PricingGroup pg) {
        this.pg = pg;
    }

    @Override
    public void run() {
        PricingGroupMapper totalMapper = ApplicationContextHelper.getBeanClass(PricingGroupMapper.class);
        List<PricingGroup> pricingGroups = totalMapper.selectList(new QueryWrapper<PricingGroup>().eq("user_id", pg.getUserId()).eq("city_id", pg.getCityId()));
        if(pricingGroups.size()!=0){
            totalMapper.delete(new UpdateWrapper<PricingGroup>().eq("user_id", pg.getUserId()).eq("city_id", pg.getCityId()));
        }
        totalMapper.insert(pg);
    }
}
