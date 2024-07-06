package com.helloscala.admin.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helloscala.common.utils.PageUtil;
import com.helloscala.common.utils.SelfStrUtil;
import com.helloscala.common.vo.cache.CacheVO;
import com.helloscala.common.web.exception.ConflictException;
import com.helloscala.common.web.response.Response;
import com.helloscala.common.web.response.ResponseHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.redis.connection.RedisServerCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/system/cache")
@RequiredArgsConstructor
@Tag(name = "System cache")
public class CacheController {
    private final StringRedisTemplate redisTemplate;

    // todo api redefine
    @GetMapping(value = "/getCacheInfo")
    @Operation(summary = "get cache info", method = "GET")
    @ApiResponse(responseCode = "200", description = "get cache info")
    public Response<Map<String, Object>> getCacheInfo() {
        Properties info = (Properties) redisTemplate.execute((RedisCallback<Object>) RedisServerCommands::info);
        Properties commandStats = (Properties) redisTemplate.execute((RedisCallback<Object>) connection -> connection.serverCommands().info("commandstats"));
        if (Objects.isNull(commandStats)) {
            throw new ConflictException("Failed to get cache info!");
        }
        Object dbSize = redisTemplate.execute((RedisCallback<Object>) RedisServerCommands::dbSize);

        Map<String, Object> result = new HashMap<>(3);
        result.put("info", info);
        result.put("dbSize", dbSize);
        List<Map<String, String>> pieList = new ArrayList<>();
        commandStats.stringPropertyNames().forEach(key -> {
            Map<String, String> data = new HashMap<>(2);
            String property = commandStats.getProperty(key);
            data.put("name", StrUtil.removePrefix(key, "cmdstat_"));
            data.put("value", SelfStrUtil.removeHeadAndTail(property, "calls=", ",usec"));
            pieList.add(data);
        });
        result.put("commandStats", pieList);
        return ResponseHelper.ok(result);
    }


    @GetMapping(value = "/list")
    @Operation(summary = "List cache info", method = "GET")
    @ApiResponse(responseCode = "200", description = "List cache info")
    public Response<Page<CacheVO>> selectCacheKeysPage() {
        Page<CacheVO> cachePage = listRedisCachePage();
        return ResponseHelper.ok(cachePage);
    }

    @NotNull
    private Page<CacheVO> listRedisCachePage() {
        Set<String> keys = redisTemplate.keys("*");
        if (keys == null || keys.isEmpty()) {
            return new Page<>();
        }
        int startIndex = (int) ((PageUtil.getPageNo() - 1) * PageUtil.getPageSize());
        int endIndex = (int) Math.min(startIndex + PageUtil.getPageSize(), keys.size());
        List<String> list = new ArrayList<>(keys).subList(startIndex, endIndex);
        List<CacheVO> cacheVOS = list.stream().map(key -> {
            CacheVO cacheVO = new CacheVO();
            cacheVO.setKey(key);
            return cacheVO;
        }).toList();

        Page<CacheVO> page = new Page<>();
        page.setRecords(cacheVOS);
        page.setTotal(keys.size());
        return page;
    }

    @GetMapping(value = "/getValue/{key}")
    @Operation(summary = "Get by key", method = "GET")
    @ApiResponse(responseCode = "200", description = "Get by key")
    public Response<Object> getValue(@PathVariable(value = "key") String key) {
        String type = redisTemplate.execute(
                (RedisCallback<String>) connection -> String.valueOf(connection.keyCommands().type(key.getBytes()))
        );

        Object data = null;
        switch (Objects.requireNonNull(type)) {
            case "STRING":
                data = redisTemplate.opsForValue().get(key);
                break;
            case "LIST":
                data = redisTemplate.opsForList().range(key, 0, -1);
                break;
            case "SET":
                data = redisTemplate.boundSetOps(key).members();
                break;
            case "ZSET":
                data = redisTemplate.opsForZSet().range(key,0,-1);
                break;
            case "HASH":
                data = redisTemplate.opsForHash().entries(key);
                break;
            default:
                break;
        }
        return ResponseHelper.ok(data);
    }

    @SaCheckPermission("system:cache:delete")
    @DeleteMapping(value = "/delete/{key}")
    @Operation(summary = "Delete cache", method = "DELETE")
    @ApiResponse(responseCode = "200", description = "Delete cache")
    public Response<Boolean> deleteCache(@PathVariable(value = "key") String key) {
        Boolean delete = redisTemplate.delete(key);
        return ResponseHelper.ok(delete);
    }

}
