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
        totalMapper.insert(pg);
    }
}
