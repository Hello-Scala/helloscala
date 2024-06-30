package com.helloscala.admin.controller;


import cn.dev33.satoken.annotation.SaCheckPermission;
import com.helloscala.common.annotation.OperationLogger;
import com.helloscala.common.entity.WebConfig;
import com.helloscala.common.service.WebConfigService;
import com.helloscala.common.web.response.EmptyResponse;
import com.helloscala.common.web.response.Response;
import com.helloscala.common.web.response.ResponseHelper;
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
    public Response<WebConfig> getWebConfig() {
        WebConfig webConfig = webConfigService.getWebConfig();
        return ResponseHelper.ok(webConfig);
    }

    @PutMapping(value = "/update")
    @SaCheckPermission("system:webConfig:update")
    @Operation(summary = "Update website config", method = "PUT")
    @ApiResponse(responseCode = "200", description = "Update website config")
    @OperationLogger(value = "Update website config")
    public EmptyResponse updateWebConfig(@RequestBody WebConfig webConfig) {
        webConfigService.updateWebConfig(webConfig);
        return ResponseHelper.ok();
    }
}

