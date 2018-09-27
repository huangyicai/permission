package com.mmall.component;

import com.mmall.dao.SysUserInfoMapper;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component("applicationContextHelper")
public class ApplicationContextHelper implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    public void setApplicationContext(ApplicationContext context) throws BeansException {
        applicationContext = context;
    }
    public static <T> T getBeanClass(Class<T> clazz){
        if(applicationContext==null){
            return null;
        }
        return applicationContext.getBean(clazz);
    }
}
