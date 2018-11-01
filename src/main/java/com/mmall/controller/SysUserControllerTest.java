package com.mmall.controller;

import com.mmall.shiro.realms.FnShiroRealm;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.SimpleAccountRealm;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;

public class SysUserControllerTest {

    @Test
    public void testA(){
        FnShiroRealm fnShiroRealm = new FnShiroRealm();
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName("md5");
        hashedCredentialsMatcher.setHashIterations(1024);
        fnShiroRealm.setCredentialsMatcher(hashedCredentialsMatcher);
        //构建SecurityManager环境
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setRealm(fnShiroRealm);
        //主体提交认证请求
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject = SecurityUtils.getSubject();

        UsernamePasswordToken token = new UsernamePasswordToken("admin","123456");
        subject.login(token);

        System.out.println("is:"+subject.isAuthenticated() );
        System.out.println(subject.getSession().getId().toString());
        subject.checkRole("admin");
        subject.checkPermissions("user:save");
        /*subject.logout();
        System.out.println( );*/
    }

    public static void main(String[] args) {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss:sss");//格式化为2017-10
        Calendar calendar = Calendar.getInstance();//得到Calendar实例
        calendar.add(Calendar.MONTH, 6);//把月份减三个月
        Date starDate = calendar.getTime();//得到时间赋给Data
        String stardtr = formatter.format(starDate);//使用格式化Data
        System.out.println(starDate.getTime());//显示
        System.out.println(stardtr);//显示

    }

}