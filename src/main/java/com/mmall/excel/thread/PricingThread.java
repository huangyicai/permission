package com.mmall.excel.thread;

import com.mmall.component.ApplicationContextHelper;
import com.mmall.service.TotalService;

/**
 * y一键定价
 */
public class PricingThread extends Thread {

    private Integer totalId;

    public PricingThread(Integer totalId) {
        this.totalId = totalId;
    }

    @Override
    public void run() {
        TotalService totalService = ApplicationContextHelper.getBeanClass(TotalService.class);
        totalService.getPricing(totalId);
    }
}
