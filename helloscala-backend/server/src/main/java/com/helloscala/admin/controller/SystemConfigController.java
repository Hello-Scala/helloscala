package com.helloscala.admin.controller;


import cn.dev33.satoken.annotation.SaCheckPermission;
import com.helloscala.common.annotation.OperationLogger;
import com.helloscala.common.ResponseResult;
import com.helloscala.common.entity.SystemConfig;
import com.helloscala.common.service.SystemConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/system/config")
@Tag(name = "System config management")
@RequiredArgsConstructor
public class SystemConfigController {

    private final SystemConfigService systemConfigService;

    @RequestMapping(value = "/getConfig",method = RequestMethod.GET)
    @Operation(summary = "Get system config", method = "GET")
    @ApiResponse(responseCode = "200", description = "Get system config")
    public ResponseResult getSystemConfig(){
        return systemConfigService.getSystemConfig();
    }

    @RequestMapping(value = "/update",method = RequestMethod.PUT)
    @SaCheckPermission("system:config:update")
    @Operation(summary = "Update system config", method = "PUT")
    @ApiResponse(responseCode = "200", description = "Update system config")
    @OperationLogger(value = "Update system config")
    public ResponseResult updateSystemConfig(@RequestBody SystemConfig systemConfig){
        return systemConfigService.updateSystemConfig(systemConfig);
    }
}