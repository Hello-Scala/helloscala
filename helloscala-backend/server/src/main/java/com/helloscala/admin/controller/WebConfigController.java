package com.helloscala.admin.controller;


import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.StpUtil;
import com.helloscala.admin.controller.request.BOUpdateWebConfigRequest;
import com.helloscala.admin.controller.view.BOWebConfigView;
import com.helloscala.admin.service.BOWebConfigService;
import com.helloscala.common.annotation.OperationLogger;
import com.helloscala.common.web.response.EmptyResponse;
import com.helloscala.common.web.response.Response;
import com.helloscala.common.web.response.ResponseHelper;
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
@Tag(name = "Website config management")
@RequiredArgsConstructor
public class WebConfigController {

    private final BOWebConfigService webConfigService;

    @GetMapping(value = "/")
    @Operation(summary = "List website config", method = "GET")
    @ApiResponse(responseCode = "200", description = "List website config")
    public Response<BOWebConfigView> getWebConfig() {
        BOWebConfigView webConfigView = webConfigService.get();
        return ResponseHelper.ok(webConfigView);
    }

    @PutMapping(value = "/update")
    @SaCheckPermission("system:webConfig:update")
    @Operation(summary = "Update website config", method = "PUT")
    @ApiResponse(responseCode = "200", description = "Update website config")
    @OperationLogger(value = "Update website config")
    public EmptyResponse updateWebConfig(@RequestBody BOUpdateWebConfigRequest request) {
        String userId = StpUtil.getLoginIdAsString();
        webConfigService.update(userId, request);
        return ResponseHelper.ok();
    }
}

