package com.helloscala.admin.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helloscala.common.utils.PageUtil;
import com.helloscala.common.utils.SelfStrUtil;
import com.helloscala.admin.controller.view.BOCacheView;
import com.helloscala.common.web.exception.ConflictException;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.redis.connection.RedisServerCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author Steve Zou
 */
@Service
@RequiredArgsConstructor
public class BOSystemCacheService {
    private final StringRedisTemplate redisTemplate;

    @NotNull
    public BORedisCacheStateView getCacheState() {
        Properties info = (Properties) redisTemplate.execute((RedisCallback<Object>) RedisServerCommands::info);
        Properties commandStats = (Properties) redisTemplate.execute((RedisCallback<Object>) connection -> connection.serverCommands().info("commandstats"));
        if (Objects.isNull(commandStats)) {
            throw new ConflictException("Failed to get cache info!");
        }
        Long dbSize = redisTemplate.execute(RedisServerCommands::dbSize);
        List<Map<String, String>> commandStatsList = commandStats.stringPropertyNames().stream().map(key -> {
            Map<String, String> data = new HashMap<>(2);
            String property = commandStats.getProperty(key);
            data.put("name", StrUtil.removePrefix(key, "cmdstat_"));
            data.put("value", SelfStrUtil.removeHeadAndTail(property, "calls=", ",usec"));
            return data;
        }).toList();

        BORedisCacheStateView redisCacheStateView = new BORedisCacheStateView();
        redisCacheStateView.setInfo(info);
        redisCacheStateView.setCommandStats(commandStatsList);
        redisCacheStateView.setDbSize(dbSize);
        return redisCacheStateView;
    }


    @NotNull
    public Page<BOCacheView> listRedisCachePage() {
        Set<String> keys = redisTemplate.keys("*");
        if (keys == null || keys.isEmpty()) {
            return new Page<>();
        }
        int startIndex = (int) ((PageUtil.getPageNo() - 1) * PageUtil.getPageSize());
        int endIndex = (int) Math.min(startIndex + PageUtil.getPageSize(), keys.size());
        List<String> list = new ArrayList<>(keys).subList(startIndex, endIndex);
        List<BOCacheView> cacheVOS = list.stream().map(key -> {
            BOCacheView cacheVO = new BOCacheView();
            cacheVO.setKey(key);
            return cacheVO;
        }).toList();

        Page<BOCacheView> page = new Page<>();
        page.setRecords(cacheVOS);
        page.setTotal(keys.size());
        return page;
    }

    public boolean delete(String key) {
        Boolean result = redisTemplate.delete(key);
        return Optional.ofNullable(result).orElse(false);
    }

    @Nullable
    public Object getValue(String key) {
        String type = redisTemplate.execute(
                (RedisCallback<String>) connection -> String.valueOf(connection.keyCommands().type(key.getBytes()))
        );

        return getData(key, type);
    }

    @Nullable
    private Object getData(String key, String type) {
        return switch (Objects.requireNonNull(type)) {
            case "STRING" -> redisTemplate.opsForValue().get(key);
            case "LIST" -> redisTemplate.opsForList().range(key, 0, -1);
            case "SET" -> redisTemplate.boundSetOps(key).members();
            case "ZSET" -> redisTemplate.opsForZSet().range(key, 0, -1);
            case "HASH" -> redisTemplate.opsForHash().entries(key);
            default -> null;
        };
    }

}
