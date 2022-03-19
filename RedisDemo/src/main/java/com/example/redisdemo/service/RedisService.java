package com.example.redisdemo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * redis工具类
 */
@Component
public class RedisService {

    private RedisTemplate redisTemplate;

    /**
     * 写入缓存
     * @param key
     * @param value
     * @return
     */
    public boolean set(final String key, Object value) {
        boolean result =false;
        try {
            ValueOperations valueOperations = redisTemplate.opsForValue();
            valueOperations.set(key,value);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 写入缓存并设置过期时间
     * @param key
     * @param value
     * @param expireTime
     * @return
     */
    public boolean setEx(final String key, Object value, long expireTime) {
        boolean result = false;
        try {
            ValueOperations valueOperations = redisTemplate.opsForValue();
            valueOperations.set(key,value);
            valueOperations.getAndExpire(key, expireTime, TimeUnit.SECONDS);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 判断是否有对应的key
     * @param key
     * @return
     */
    public boolean exists(final String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 读取缓存
     * @param key
     * @return
     */
    public Object get(final String key) {
        Object result = null;
        ValueOperations valueOperations = redisTemplate.opsForValue();
        result = valueOperations.get(key);
        return result;
    }

    /**
     * 删除key
     * @param key
     * @return
     */
    public boolean remove(final String key) {
        if(exists(key)) {
            boolean delete = redisTemplate.delete(key);
            return delete;
        }
        return false;
    }

    public long incr(String key, long time) {
        Long count = redisTemplate.opsForValue().increment(key, 1);
        if (count == 1) {
            //写入缓存，并设置过期时间
            redisTemplate.expire(key,time,TimeUnit.SECONDS);
        }
        return count;
    }

    /**
     * redis默认使用JdkSerializationRedisSerializer来进行序列化，我们修改成StringRedisSerializer
     * @param redisTemplate
     */
    @Autowired(required = false)
    public void setRedisTemplate(RedisTemplate redisTemplate) {
        RedisSerializer stringSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringSerializer);
        redisTemplate.setValueSerializer(stringSerializer);
        redisTemplate.setHashKeySerializer(stringSerializer);
        redisTemplate.setHashValueSerializer(stringSerializer);
        this.redisTemplate = redisTemplate;
    }
}
