package com.mmall.excel.thread;

import com.mmall.component.ApplicationContextHelper;
import com.mmall.dao.PricingGroupMapper;
import com.mmall.model.PricingGroup;



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
