package com.helloscala.common.service.impl;


import com.helloscala.common.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@SuppressWarnings(value = {"unchecked", "rawtypes"})
@Service
@RequiredArgsConstructor
public class RedisServiceImpl implements RedisService {

    public final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void setCacheObject(final String key, final Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public void setCacheObject(final String key, final Object value, final Integer timeout, final TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
    }

    @Override
    public boolean expire(final String key, final long timeout, final TimeUnit unit) {
        return Boolean.TRUE.equals(redisTemplate.expire(key, timeout, unit));
    }

    @Override
    public Set<Object> getCacheSet(final String key) {
        return redisTemplate.boundSetOps(key).members();
    }

    @Override
    public Object getCacheObject(final String key) {
        ValueOperations<String, Object> operation = redisTemplate.opsForValue();
        return operation.get(key);
    }

    @Override
    public boolean deleteObject(final String key) {
        return Boolean.TRUE.equals(redisTemplate.delete(key));
    }

    @Override
    public Optional<Long> deleteObject(final Collection collection) {
        return Optional.ofNullable(redisTemplate.delete(collection));
    }

    @Override
    public long setCacheList(final String key, final List<Object> dataList) {
        Long count = redisTemplate.opsForList().rightPushAll(key, dataList);
        return count == null ? 0 : count;
    }

    @Override
    public List<Object> getCacheList(final String key) {
        return redisTemplate.opsForList().range(key, 0, -1);
    }

    @Override
    public void setCacheMap(final String key, final Map<String, Object> dataMap) {
        if (dataMap != null) {
            redisTemplate.opsForHash().putAll(key, dataMap);
        }
    }

    @Override
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

    @Override
    public Set<String> diff(String key1, String key2) {
        Set<Object> difference = redisTemplate.opsForSet().difference(key1, key2);
        if (Objects.isNull(difference)) {
            return new HashSet<>();
        }
        return difference.stream().map(Object::toString).collect(Collectors.toSet());
    }

    @Override
    public Boolean sIsMember(String key, Object value) {
        return redisTemplate.opsForSet().isMember(key, value);
    }

    @Override
    public Long hIncr(String key, String hashKey, Long delta) {
        return redisTemplate.opsForHash().increment(key, hashKey, delta);
    }

    @Override
    public Long hDecr(String key, String hashKey, Long delta) {
        return redisTemplate.opsForHash().increment(key, hashKey, -delta);
    }

    @Override
    public Set<Object> sMembers(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    @Override
    public Long sRemove(String key, Object... values) {
        return redisTemplate.opsForSet().remove(key, values);
    }

    @Override
    public Object hGet(String key, String hashKey) {
        return redisTemplate.opsForHash().get(key, hashKey);
    }

    @Override
    public Long incr(String key, long delta) {
        return redisTemplate.opsForValue().increment(key, delta);
    }

    @Override
    public Long sAdd(String key, Object... values) {
        return redisTemplate.opsForSet().add(key, values);
    }

    @Override
    public Long getCacheSetKeyNumber(String key) {
        return redisTemplate.opsForSet().size(key);
    }

    @Override
    public void incrArticle(Long id, String key, String ip) {
        Map<String, Object> map = getCacheMap(key);
        List<String> ipList = (List<String>) map.get(id.toString());
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

    @Override
    public void redisTimer() {
        redisTemplate.opsForValue().get("heartbeat");
    }

    @Override
    public boolean hasKey(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }


}
