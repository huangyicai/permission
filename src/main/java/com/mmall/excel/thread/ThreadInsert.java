package com.mmall.excel.thread;

import com.mmall.component.ApplicationContextHelper;
import com.mmall.constants.LevelConstants;
import com.mmall.model.SysUserInfo;
import com.mmall.model.params.UserInfoExpressParm;
import com.mmall.service.serviceImpl.SysUserServiceImpl;

public class ThreadInsert extends Thread{

    private UserInfoExpressParm ui;
    private SysUserInfo parent;
    private Integer id;

    public ThreadInsert(UserInfoExpressParm ui, SysUserInfo parent, Integer id) {
        this.ui = ui;
        this.parent = parent;
        this.id = id;
    }

    @Override
    public void run() {
        SysUserServiceImpl expressUserService = ApplicationContextHelper.getBeanClass(SysUserServiceImpl.class);
        try {
            expressUserService.register(ui, parent, LevelConstants.SERVICE,5);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
