package it.gov.pagopa.mockconfig.repository;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RedisRepository {

    @Autowired
    @Qualifier("object")
    private RedisTemplate<String, Object> redisTemplateObj;


    public void save(String key, Object value, long ttl) {
        redisTemplateObj.opsForValue().set(key, value, Duration.ofMinutes(ttl));
    }

    public Object get(String key) {
        return redisTemplateObj.opsForValue().get(key);
    }

    public Object getTTL(String key) {
        return redisTemplateObj.getExpire(key, TimeUnit.MINUTES);
    }
    public void save_(String key, Object value, long ttl) {
        redisTemplateObj.opsForValue().set(key, value, Duration.ofMinutes(ttl));
    }

    public void remove(String key) {
        redisTemplateObj.delete(key);
    }
    @Async
    public void pushToRedisAsync(String key, Object object) {
        try {
            log.info("saving {} on redis", key);
            save(key, object, 1440);
            log.info("saved {} on redis", key);
        } catch (Exception e) {
            log.error("could not save " + key + "on redis", e);
        }
    }

    public String getStringByKeyId(String keyId) {
        Object v = null;
        try {
            v = get(keyId);
        } catch (Exception e) {
            log.error("could not get key " + keyId + " from redis", e);
        }
        if (v != null) {
            return (String) v;
        } else {
            return null;
        }
    }

    public Boolean getBooleanByKeyId(String keyId) {
        Object v = null;
        try {
            v = get(keyId);
        } catch (Exception e) {
            log.error("could not get key " + keyId + " from redis", e);
        }
        if (v != null) {
            return (Boolean) v;
        } else {
            return Boolean.FALSE;
        }
    }
}