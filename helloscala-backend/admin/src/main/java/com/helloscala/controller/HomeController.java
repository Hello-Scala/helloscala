package com.helloscala.controller;


import com.helloscala.common.ResponseResult;
import com.helloscala.service.impl.HomeServiceImpl;
import com.helloscala.vo.service.SystemHardwareInfoVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/system/home")
@Tag(name = "Admin home page")
@RequiredArgsConstructor
public class HomeController {

    private final HomeServiceImpl homeService;

    @GetMapping(value = "/init")
    @Operation(summary = "Home page statistics", method = "GET")
    @ApiResponse(responseCode = "200", description = "Home page statistics")
    public ResponseResult init() {
        return ResponseResult.success(homeService.init());
    }

    @GetMapping(value = "/lineCount")
    @Operation(summary = "Article, user ip, comments statistics", method = "GET")
    @ApiResponse(responseCode = "200", description = "Article, user ip, comments statistics")
    public ResponseResult lineCount() {
        return ResponseResult.success(homeService.lineCount());
    }

    @GetMapping(value = "/systemInfo")
    @Operation(summary = "System running state", method = "GET")
    @ApiResponse(responseCode = "200", description = "System running state")
    public ResponseResult systemInfo() {
        SystemHardwareInfoVO systemHardwareInfoVO = new SystemHardwareInfoVO();
        try {
            systemHardwareInfoVO.copyTo();
        } catch (Exception e) {
            log.error("Failed to get system hardware info", e);
        }
        return ResponseResult.success(systemHardwareInfoVO);
    }
}
