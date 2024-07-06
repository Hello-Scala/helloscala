package com.helloscala.admin.controller;


import com.helloscala.common.service.impl.HomeServiceImpl;
import com.helloscala.common.vo.service.SystemHardwareInfoVO;
import com.helloscala.common.vo.system.SystemHomeDataVO;
import com.helloscala.common.web.response.Response;
import com.helloscala.common.web.response.ResponseHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/system/home")
@Tag(name = "Admin home page")
@RequiredArgsConstructor
public class HomeController {

    private final HomeServiceImpl homeService;

    // todo refactor
    @GetMapping(value = "/init")
    @Operation(summary = "Home page statistics", method = "GET")
    @ApiResponse(responseCode = "200", description = "Home page statistics")
    public Response<SystemHomeDataVO> init() {
        SystemHomeDataVO systemHomeDataVO = homeService.init();
        return ResponseHelper.ok(systemHomeDataVO);
    }

    @GetMapping(value = "/lineCount")
    @Operation(summary = "Article, user ip, comments statistics", method = "GET")
    @ApiResponse(responseCode = "200", description = "Article, user ip, comments statistics")
    public Response<Map<String, Integer>> lineCount() {
        Map<String, Integer> summaryMap = homeService.lineCount();
        return ResponseHelper.ok(summaryMap);
    }

    @GetMapping(value = "/systemInfo")
    @Operation(summary = "System running state", method = "GET")
    @ApiResponse(responseCode = "200", description = "System running state")
    public Response<SystemHardwareInfoVO> systemInfo() {
        SystemHardwareInfoVO systemHardwareInfoVO = new SystemHardwareInfoVO();
        systemHardwareInfoVO.copyTo();
        return ResponseHelper.ok(systemHardwareInfoVO);
    }
}
