package com.helloscala.controller;


import com.helloscala.common.ResponseResult;
import com.helloscala.service.impl.HomeServiceImpl;
import com.helloscala.vo.service.SystemHardwareInfoVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/system/home")
@Tag(name = "后台首页")
@RequiredArgsConstructor
public class HomeController {

    private final HomeServiceImpl homeService;

    @GetMapping(value = "/init")
    @Operation(summary = "首页各种统计信息", method = "GET")
    @ApiResponse(responseCode = "200", description = "首页各种统计信息")
    public ResponseResult init() {
        return ResponseResult.success(homeService.init());
    }

    @GetMapping(value = "/lineCount")
    @Operation(summary = "首页文章、ip用户、留言统计", method = "GET")
    @ApiResponse(responseCode = "200", description = "首页文章、ip用户、留言统计")
    public ResponseResult lineCount() {
        return ResponseResult.success(homeService.lineCount());
    }

    @GetMapping(value = "/systemInfo")
    @Operation(summary = "服务器监控", method = "GET")
    @ApiResponse(responseCode = "200", description = "服务器监控")
    public ResponseResult systemInfo() {
        SystemHardwareInfoVO systemHardwareInfoVO = new SystemHardwareInfoVO();
        try {
            systemHardwareInfoVO.copyTo();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseResult.success(systemHardwareInfoVO);
    }
}
