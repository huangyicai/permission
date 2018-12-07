package com.mmall.excel.thread;

import com.mmall.component.ApplicationContextHelper;
import com.mmall.model.Response.Result;
import com.mmall.model.Total;
import com.mmall.service.TotalService;

import java.util.List;

/**
 * y一键定价
 */
public class PricingThread extends Thread {

    private Integer totalId;

    List<Result> list;

    public PricingThread(Integer totalId,List<Result> list) {
        this.totalId = totalId;
        this.list=list;
    }

    @Override
    public void run() {
        TotalService totalService = ApplicationContextHelper.getBeanClass(TotalService.class);
        Result pricing = totalService.getPricing(totalId);
        Total byId = totalService.getById(totalId);
        if(byId==null){
            byId=new Total();
        }
        pricing.setData(byId);
        list.add(pricing);
    }
}
