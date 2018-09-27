package com.mmall.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Set;

/**
 *
 * @author hyc
 * @since 2018-09-15
 */
@Component
public class JedisUtil {
    @Autowired
    private JedisPool jedisPool;

    private Jedis getResource(){
        return jedisPool.getResource();
    }

    public byte[] set(byte[] key, byte[] value) {
        Jedis jedis = getResource();
        try{
            jedis.set(key,value);
            return value;
        }finally {
            jedis.close();
        }
    }

    public void expire(byte[] key, Integer time) {
        Jedis jedis = getResource();
        try{
            jedis.expire(key,time);
        }finally {
            jedis.close();
        }
    }

    public byte[] get(byte[] key) {
        Jedis jedis = getResource();
        try{
            byte[] bytes = jedis.get(key);
            return bytes;
        }finally {
            jedis.close();
        }
    }

    public void del(byte[] key) {
        Jedis jedis = getResource();
        try{
            jedis.del(key);
        }finally {
            jedis.close();
        }
    }

    public Set<byte[]> getKeys(String shiroSessionPrefix) {
        Jedis jedis = getResource();
        try{
            return jedis.keys((shiroSessionPrefix+"*").getBytes());

        }finally {
            jedis.close();
        }
    }
}
