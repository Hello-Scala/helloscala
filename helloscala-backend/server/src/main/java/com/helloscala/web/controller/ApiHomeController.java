package com.helloscala.web.controller;


import com.helloscala.common.ResponseResult;
import com.helloscala.web.service.ApiHomeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
@Tag(name = "Home page management")
public class ApiHomeController {

    private final ApiHomeService homeService;


    @RequestMapping(value = "/report",method = RequestMethod.GET)
    @Operation(summary = "Add view count", method = "GET")
    @ApiResponse(responseCode = "200", description = "Add view count")
    public ResponseResult report(){
        return homeService.report();
    }

    @GetMapping("/webSiteInfo")
    @Operation(summary = "get website info", method = "GET")
    @ApiResponse(responseCode = "200", description = "get website info")
    public ResponseResult getWebSiteInfo(){
        return homeService.getWebSiteInfo();
    }

    @GetMapping("/")
    @Operation(summary = "Get home data", method = "GET")
    @ApiResponse(responseCode = "200", description = "Get home data")
    public ResponseResult getHomeData(){
        return homeService.getHomeData();
    }

    @GetMapping("/hot")
    @Operation(summary = "Get hot news", method = "GET")
    @ApiResponse(responseCode = "200", description = "Get hot news")
    public ResponseResult hot(@RequestParam(name = "type", required = false) String type){
        return homeService.hot(type);
    }
}

