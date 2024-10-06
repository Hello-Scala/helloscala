package com.helloscala.common.cache;


import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@SuppressWarnings(value = {"unchecked", "rawtypes"})
@Service
@RequiredArgsConstructor
public class RedisService {

    public final RedisTemplate<String, Object> redisTemplate;

    public void setCacheObject(final String key, final Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public void setCacheObject(final String key, final Object value, final Integer timeout, final TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
    }

    public boolean expire(final String key, final long timeout, final TimeUnit unit) {
        return Boolean.TRUE.equals(redisTemplate.expire(key, timeout, unit));
    }

    public Set<Object> getCacheSet(final String key) {
        return redisTemplate.boundSetOps(key).members();
    }

    public Object getCacheObject(final String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public boolean deleteObject(final String key) {
        return Boolean.TRUE.equals(redisTemplate.delete(key));
    }

    public Optional<Long> deleteObject(final Collection collection) {
        return Optional.ofNullable(redisTemplate.delete(collection));
    }

    public long setCacheList(final String key, final List<Object> dataList) {
        Long count = redisTemplate.opsForList().rightPushAll(key, dataList);
        return count == null ? 0 : count;
    }

    public List<Object> getCacheList(final String key) {
        return redisTemplate.opsForList().range(key, 0, -1);
    }

    public void setCacheMap(final String key, final Map<String, Object> dataMap) {
        if (dataMap != null) {
            redisTemplate.opsForHash().putAll(key, dataMap);
        }
    }

    public Map<String, Object> getCacheMap(final String key) {
        Map<Object, Object> entries = redisTemplate.opsForHash().entries(key);
        return toStringObjectMap(entries);
    }

    private static Map<String, Object> toStringObjectMap(Map<Object, Object> entries) {
        return entries.entrySet().stream().collect(Collectors.toMap(e -> e.getKey().toString(), Map.Entry::getValue));
    }

    public <T> void setCacheMapValue(final String key, final String hKey, final T value) {
        redisTemplate.opsForHash().put(key, hKey, value);
    }

    public <T> T getCacheMapValue(final String key, final String hKey) {
        HashOperations<String, String, T> opsForHash = redisTemplate.opsForHash();
        return opsForHash.get(key, hKey);
    }

    public <T> List<T> getMultiCacheMapValue(final String key, final Collection<Object> hKeys) {
        return (List<T>) redisTemplate.opsForHash().multiGet(key, hKeys);
    }

    public Collection<String> keys(final String pattern) {
        return redisTemplate.keys(pattern);
    }

    public Set<String> diff(String key1, String key2) {
        Set<Object> difference = redisTemplate.opsForSet().difference(key1, key2);
        if (Objects.isNull(difference)) {
            return new HashSet<>();
        }
        return difference.stream().map(Object::toString).collect(Collectors.toSet());
    }


    public Boolean isMember(String key, Object value) {
        return redisTemplate.opsForSet().isMember(key, value);
    }

    public Long hIncr(String key, String hashKey, Long delta) {
        return redisTemplate.opsForHash().increment(key, hashKey, delta);
    }

    public Long hDecr(String key, String hashKey, Long delta) {
        return redisTemplate.opsForHash().increment(key, hashKey, -delta);
    }

    public Set<Object> sMembers(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    public Long sRemove(String key, Object... values) {
        return redisTemplate.opsForSet().remove(key, values);
    }

    public Object hGet(String key, String hashKey) {
        return redisTemplate.opsForHash().get(key, hashKey);
    }

    public Long incr(String key, long delta) {
        return redisTemplate.opsForValue().increment(key, delta);
    }

    public Long sAdd(String key, Object... values) {
        return redisTemplate.opsForSet().add(key, values);
    }

    public Long getCacheSetKeyNumber(String key) {
        return redisTemplate.opsForSet().size(key);
    }

    public void incrArticle(String id, String key, String ip) {
        Map<String, Object> map = getCacheMap(key);
        List<String> ipList = (List<String>) map.get(id);
        if (ipList != null) {
            if (!ipList.contains(ip)) {
                ipList.add(ip);
            }
        } else {
            ipList = new ArrayList<>();
            ipList.add(ip);
        }
        map.put(id.toString(), ipList);
        setCacheMap(key, map);
    }

    public void redisTimer() {
        redisTemplate.opsForValue().get("heartbeat");
    }

    public boolean hasKey(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }
}
