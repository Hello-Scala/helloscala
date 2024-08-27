package com.helloscala.admin.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helloscala.admin.service.BORedisCacheStateView;
import com.helloscala.admin.service.BOSystemCacheService;
import com.helloscala.admin.controller.view.BOCacheView;
import com.helloscala.common.web.response.Response;
import com.helloscala.common.web.response.ResponseHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/system/cache")
@RequiredArgsConstructor
public class CacheController {
    private final BOSystemCacheService systemCacheService;

    // todo api redefine
    @GetMapping(value = "/getCacheInfo")
    @Operation(summary = "get cache info", method = "GET")
    @ApiResponse(responseCode = "200", description = "get cache info")
    public Response<BORedisCacheStateView> getCacheInfo() {
        BORedisCacheStateView redisCacheStateView = systemCacheService.getCacheState();
        return ResponseHelper.ok(redisCacheStateView);
    }


    @GetMapping(value = "/list")
    @Operation(summary = "List cache info", method = "GET")
    @ApiResponse(responseCode = "200", description = "List cache info")
    public Response<Page<BOCacheView>> selectCacheKeysPage() {
        Page<BOCacheView> cachePage = systemCacheService.listRedisCachePage();
        return ResponseHelper.ok(cachePage);
    }


    @GetMapping(value = "/getValue/{key}")
    @Operation(summary = "Get by key", method = "GET")
    @ApiResponse(responseCode = "200", description = "Get by key")
    public Response<Object> getValue(@PathVariable(value = "key") String key) {
        Object data = systemCacheService.getValue(key);
        return ResponseHelper.ok(data);
    }

    @SaCheckPermission("system:cache:delete")
    @DeleteMapping(value = "/delete/{key}")
    @Operation(summary = "Delete cache", method = "DELETE")
    @ApiResponse(responseCode = "200", description = "Delete cache")
    public Response<Boolean> deleteCache(@PathVariable(value = "key") String key) {
        Boolean delete = systemCacheService.delete(key);
        return ResponseHelper.ok(delete);
    }

}
