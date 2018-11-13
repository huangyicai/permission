package com.mmall.config;
import java.util.Set;

import javax.websocket.Endpoint;
import javax.websocket.server.ServerEndpointConfig;
/**
 * @author Huang YiCai
 * @create 2018/11/12  9:56
 */
public class WebConfig {
    /**
     * 接口式编程
     */
    public Set<ServerEndpointConfig> getEndpointConfigs(Set<Class<? extends Endpoint>> endpointClasses) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * 注解式编程
     */
    public Set<Class<?>> getAnnotatedEndpointClasses(Set<Class<?>> scanned) {
        System.out.println("config>>>>>"+scanned.size());
        return scanned;
    }

}
