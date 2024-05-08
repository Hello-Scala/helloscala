package com.helloscala.controller;


import com.helloscala.common.ResponseResult;
import com.helloscala.service.ApiHomeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
@Tag(name = "门户首页管理")
public class ApiHomeController {

    private final ApiHomeService homeService;


    @RequestMapping(value = "/report",method = RequestMethod.GET)
    @Operation(summary = "增加访问量", method = "GET")
    @ApiResponse(responseCode = "200", description = "增加访问量")
    public ResponseResult report(){
        return homeService.report();
    }

    @GetMapping("/webSiteInfo")
    @Operation(summary = "网站相关信息", method = "GET")
    @ApiResponse(responseCode = "200", description = "网站相关信息")
    public ResponseResult getWebSiteInfo(){
        return homeService.getWebSiteInfo();
    }

    @GetMapping("/")
    @Operation(summary = "首页共享数据", method = "GET")
    @ApiResponse(responseCode = "200", description = "首页共享数据")
    public ResponseResult getHomeData(){
        return homeService.getHomeData();
    }

    @GetMapping("/hot")
    @Operation(summary = "获取各大平台热搜", method = "GET")
    @ApiResponse(responseCode = "200", description = "获取各大平台热搜")
    public ResponseResult hot(@RequestParam(name = "type", required = false) String type){
        return homeService.hot(type);
    }

}

