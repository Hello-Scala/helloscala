package com.helloscala.controller;


import cn.dev33.satoken.annotation.SaCheckPermission;
import com.helloscala.annotation.OperationLogger;
import com.helloscala.common.ResponseResult;
import com.helloscala.entity.WebConfig;
import com.helloscala.service.WebConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/system/webConfig")
@Tag(name = "网站配置管理")
@RequiredArgsConstructor
public class WebConfigController {

    private final WebConfigService webConfigService;

    @GetMapping(value = "/")
    @Operation(summary = "网站配置列表", method = "GET")
    @ApiResponse(responseCode = "200", description = "网站配置列表")
    public ResponseResult getWebConfig() {
        return webConfigService.getWebConfig();
    }

    @PutMapping(value = "/update")
    @SaCheckPermission("system:webConfig:update")
    @Operation(summary = "修改网站配置", method = "PUT")
    @ApiResponse(responseCode = "200", description = "网站配置列表")
    @OperationLogger(value = "修改网站配置")
    public ResponseResult updateWebConfig(@RequestBody WebConfig webConfig) {
        return webConfigService.updateWebConfig(webConfig);
    }
}

