package com.helloscala.common.service;

import java.util.*;
import java.util.concurrent.TimeUnit;

public interface RedisService {
    void setCacheObject(String key, Object value);

    void setCacheObject(String key, Object value, Integer timeout, TimeUnit timeUnit);

    boolean expire(String key, long timeout, TimeUnit unit);

    Set<Object> getCacheSet( String key);

    Object getCacheObject(String key);

    boolean deleteObject(final String key);

    public Optional<Long> deleteObject(final Collection collection);

    long setCacheList(String key, List<Object> dataList);

    List<Object> getCacheList(final String key);

    void setCacheMap(String key, Map<String, Object> dataMap);

    Map<String, Object> getCacheMap(final String key);

    Set<String> diff(String key1, String key2);

    Boolean sIsMember(String key, Object value);

    Long hIncr(String key, String hashKey, Long delta);

    Long hDecr(String key, String hashKey, Long delta);

    Set<Object> sMembers(String key);

    Long sRemove(String key, Object... values);

    Object hGet(String key, String hashKey);

    Long incr(String key, long delta);

    Long sAdd(String key, Object... values);

    Long getCacheSetKeyNumber(String key);

    void incrArticle(String id,String key,String ip);

    void redisTimer();


    boolean hasKey(String key);

    Collection<String> keys(String s);
}
