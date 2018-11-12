package com.mmall.shiro;

import com.google.common.collect.Sets;
import com.mmall.util.JedisUtil;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.springframework.util.CollectionUtils;
import org.springframework.util.SerializationUtils;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Collection;
import java.util.Set;


/**
 *
 * @author hyc
 * @since 2018-09-15
 */
public class RedisSeessionDao extends AbstractSessionDAO {
    @Resource
    private JedisUtil jedisUtil;

    private final String SHIRO_SESSION_PREFIX="session";

    //过期时间
    private final Integer BE_OVERDUE_TIME = 1000*60*120;

    private byte[] getKey(String key){
        return (SHIRO_SESSION_PREFIX+key).getBytes();
    }

    private void saveSession(Session session){
        if(session!=null&&session.getId()!=null){
            byte[] key = getKey(session.getId().toString());
            //序列化为byte[]
            byte[] value = SerializationUtils.serialize(session);
            jedisUtil.set(key,value);
            jedisUtil.expire(key,BE_OVERDUE_TIME);
        }
    }

    /**
     * 创建session
     * @param session
     * @return
     */
    protected Serializable doCreate(Session session) {
        Serializable sessionId = generateSessionId(session);
        assignSessionId(session,sessionId);
        saveSession(session);
        return sessionId;
    }

    /**
     * 获取session
     * @param sessionId
     * @return
     */
    protected Session doReadSession(Serializable sessionId) {
        if(sessionId==null){
            return null;
        }
        byte[] key = getKey(sessionId.toString());
        //序列化为byte[]
        byte[] value = jedisUtil.get(key);
        //反序列化
        return (Session) SerializationUtils.deserialize(value);
    }

    public void update(Session session) throws UnknownSessionException {
        saveSession(session);
    }

    public void delete(Session session) {
        if(session!=null&&session.getId()!=null){
            return ;
        }
        byte[] key = getKey(session.getId().toString());
        jedisUtil.del(key);
    }

    public Collection<Session> getActiveSessions() {
        Set<byte[]> keys = jedisUtil.getKeys(SHIRO_SESSION_PREFIX);
        Set<Session> sessions = Sets.newHashSet();
        if(CollectionUtils.isEmpty(keys)){
            return sessions;
        }
        for(byte[] key:keys){
            Session session = (Session)SerializationUtils.deserialize(key);
            sessions.add(session);
        }
        return sessions;
    }
}
