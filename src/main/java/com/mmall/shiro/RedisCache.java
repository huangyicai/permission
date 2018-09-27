package com.mmall.shiro;

import com.mmall.util.JedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Set;
/**
 *cache缓存
 *
 * @author hyc
 * @since 2018-09-15
 */
@Component
@Slf4j
public class RedisCache<K,V>  implements Cache<K,V> {
    private final String CACHE_PREFIX = "fn_cache";
    //过期时间
    private final Integer BE_OVERDUE_TIME = 10*60;
    @Resource
    private JedisUtil jedisUtil;

    private byte[] getKey(K k){
        if(k instanceof String ){
            return (CACHE_PREFIX+k).getBytes();
        }
        return SerializationUtils.serialize(k);
    }

    public V get(K k) throws CacheException {
        log.info("redis*************");
        byte[] value = jedisUtil.get(getKey(k));
        if(value!=null){
            return (V)SerializationUtils.deserialize(value);
        }
        return null;
    }

    public V put(K k, V v) throws CacheException {
        byte[] key = getKey(k);
        byte[] value = SerializationUtils.serialize(v);
        jedisUtil.set(key,value);
        jedisUtil.expire(key,BE_OVERDUE_TIME);
        return v;
    }

    public V remove(K k) throws CacheException {
        byte[] key = getKey(k);
        byte[] value = jedisUtil.get(key);
        jedisUtil.del(key);
        if(value!=null){
            return (V)SerializationUtils.deserialize(value);
        }
        return null;
    }

    /**
     * 全部清除  （不用重新）
     * @throws CacheException
     */
    public void clear() throws CacheException {

    }

    public int size() {
        return 0;
    }

    public Set<K> keys() {
        return null;
    }

    public Collection<V> values() {
        return null;
    }
}
