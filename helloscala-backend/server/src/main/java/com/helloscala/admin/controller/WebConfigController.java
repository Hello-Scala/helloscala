package com.helloscala.admin.controller;


import cn.dev33.satoken.annotation.SaCheckPermission;
import com.helloscala.common.annotation.OperationLogger;
import com.helloscala.common.ResponseResult;
import com.helloscala.common.entity.WebConfig;
import com.helloscala.common.service.WebConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/webConfig")
@Tag(name = "Website config management")
@RequiredArgsConstructor
public class WebConfigController {

    private final WebConfigService webConfigService;

    @GetMapping(value = "/")
    @Operation(summary = "List website config", method = "GET")
    @ApiResponse(responseCode = "200", description = "List website config")
    public ResponseResult getWebConfig() {
        return webConfigService.getWebConfig();
    }

    @PutMapping(value = "/update")
    @SaCheckPermission("system:webConfig:update")
    @Operation(summary = "Update website config", method = "PUT")
    @ApiResponse(responseCode = "200", description = "Update website config")
    @OperationLogger(value = "Update website config")
    public ResponseResult updateWebConfig(@RequestBody WebConfig webConfig) {
        return webConfigService.updateWebConfig(webConfig);
    }
}

