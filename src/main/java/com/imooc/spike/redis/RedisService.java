package com.imooc.spike.redis;

import com.alibaba.fastjson.JSON;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @Author: rammelzzz
 * @Description:
 * @Date: Created in 下午6:26 18-4-17
 * @Modified By:
 **/
@Service
public class RedisService {


    @Autowired
    private JedisPool jedisPool;
    private Logger log = LoggerFactory.getLogger(getClass());

    /**
     * 获取单个对象
     *
     * @param prefix
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T get(KeyPrefix prefix, String key, Class<T> clazz) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey = prefix.getPrefix() + ":" + key;
//            log.info("realKey={}", realKey);
            String str = jedis.get(realKey);
//            log.info("str={}", str);
            return strToBean(str, clazz);
        } finally {
            returnToPool(jedis);
        }
    }

    /**
     * 设置单个对象
     *
     * @param prefix
     * @param key
     * @param value
     * @param <T>
     * @return
     */
    public <T> boolean set(KeyPrefix prefix, String key, T value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey = prefix.getPrefix() + ":" + key;
            String str = beanToStr(value);
            int expireSeconds = prefix.expireSeconds();
            if (str == null || str.length() <= 0) {
                return false;
            } else {
                if (expireSeconds <= 0) {
                    jedis.set(realKey, str);
                } else {
                    //expireSeconds是过期时间
                    jedis.setex(realKey, expireSeconds, str);
                }
            }
        } finally {
            returnToPool(jedis);
        }
        return true;
    }

    /**
     * 判断某个键值是否存在
     *
     * @param prefix
     * @param key
     * @return
     */
    public boolean exist(KeyPrefix prefix, String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey = prefix.getPrefix() + ":" + key;
            return jedis.exists(realKey);
        } finally {
            returnToPool(jedis);
        }
    }

    /**
     * 自增一个数值
     *
     * @param prefix
     * @param key
     * @return
     */
    public long incr(KeyPrefix prefix, String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey = prefix.getPrefix() + ":" + key;
            return jedis.incr(realKey);
        } finally {
            returnToPool(jedis);
        }
    }

    /**
     * 自减
     *
     * @param prefix
     * @param key
     * @return
     */
    public long decr(KeyPrefix prefix, String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey = prefix.getPrefix() + ":" + key;
            return jedis.decr(realKey);
        } finally {
            returnToPool(jedis);
        }
    }

    /**
     * 将json字符串转换为bean
     *
     * @param str
     * @param clazz
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T strToBean(String str, Class<T> clazz) {
        if (str == null || str.length() <= 0) {
            return null;
        }
        if (clazz == int.class || clazz == Integer.class) {
            return (T) Integer.valueOf(str);
        } else if (clazz == String.class) {
            return (T) str;
        } else if (clazz == long.class || clazz == Long.class) {
            return (T) Long.valueOf(str);
        } else {
            return JSON.toJavaObject(JSON.parseObject(str), clazz);
        }
    }

    /**
     * 将一个Bean转换为字符串存到Redis
     *
     * @param src
     * @param <T>
     * @return
     */
    public static <T> String beanToStr(T src) {
        if (src == null) {
            return null;
        }
        Class<?> clazz = src.getClass();
        if (clazz == int.class || clazz == Integer.class) {
            return "" + src;
        } else if (clazz == String.class) {
            return (String) src;
        } else if (clazz == long.class || clazz == Long.class) {
            return "" + src;
        } else {
            return JSON.toJSONString(src);
        }
    }

    public Long del(KeyPrefix prefix, String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey = prefix.getPrefix() + ":" + key;
            return jedis.del(realKey);
        } finally {
            returnToPool(jedis);
        }
    }

    private void returnToPool(Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }
}
