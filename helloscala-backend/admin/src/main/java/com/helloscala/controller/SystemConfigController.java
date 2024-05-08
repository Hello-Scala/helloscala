package com.helloscala.controller;


import cn.dev33.satoken.annotation.SaCheckPermission;
import com.helloscala.annotation.OperationLogger;
import com.helloscala.common.ResponseResult;
import com.helloscala.entity.SystemConfig;
import com.helloscala.service.SystemConfigService;
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
@Tag(name = "系统配置管理")
@RequiredArgsConstructor
public class SystemConfigController {

    private final SystemConfigService systemConfigService;

    @RequestMapping(value = "/getConfig",method = RequestMethod.GET)
    @Operation(summary = "查询系统配置", method = "GET")
    @ApiResponse(responseCode = "200", description = "查询系统配置")
    public ResponseResult getSystemConfig(){
        return systemConfigService.getSystemConfig();
    }

    @RequestMapping(value = "/update",method = RequestMethod.PUT)
    @SaCheckPermission("system:config:update")
    @Operation(summary = "修改系统配置", method = "PUT")
    @ApiResponse(responseCode = "200", description = "修改系统配置")
    @OperationLogger(value = "修改系统配置")
    public ResponseResult updateSystemConfig(@RequestBody SystemConfig systemConfig){
        return systemConfigService.updateSystemConfig(systemConfig);
    }
}

