package com.ch.bojbm.global.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RedisService {
    private final StringRedisTemplate redisTemplate;

    // key를 통해 value 리턴
    public String getData(String key){
        return redisTemplate.opsForValue().get(key);
    }

    @Transactional
    public void setData(String key, String value){
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(key,value);
    }

    // 유효시간 동안만 key,value 저장
    // MILLISECOND(밀리초) 기준
    @Transactional
    public void setDataExpire(String key, String  value, long duration){
        redisTemplate.opsForValue().set(key,value,duration, TimeUnit.MILLISECONDS);
        ValueOperations<String,String> valueOperations = redisTemplate.opsForValue();
        Duration expireDuration = Duration.ofSeconds(duration);
        valueOperations.set(key, value, expireDuration);
    }

    //삭제
    @Transactional
    public void deleteData(String key){
        redisTemplate.delete(key);
    }
}
