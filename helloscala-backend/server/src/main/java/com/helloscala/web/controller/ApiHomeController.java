package com.helloscala.web.controller;


import com.helloscala.common.web.response.Response;
import com.helloscala.common.web.response.ResponseHelper;
import com.helloscala.web.controller.home.APIGetHomeInfoResponse;
import com.helloscala.web.controller.home.APIGetWebSiteInfoResponse;
import com.helloscala.web.service.APIHomeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/home")
@RequiredArgsConstructor
@Tag(name = "Home page management")
public class ApiHomeController {
    private final APIHomeService homeService;


    @RequestMapping(value = "/report", method = RequestMethod.GET)
    @Operation(summary = "Add view count", method = "GET")
    @ApiResponse(responseCode = "200", description = "Add view count")
    public Response<String> report() {
        String report = homeService.report();
        return ResponseHelper.ok(report);
    }

    @GetMapping("/webSiteInfo")
    @Operation(summary = "get website info", method = "GET")
    @ApiResponse(responseCode = "200", description = "get website info")
    public Response<APIGetWebSiteInfoResponse> getWebSiteInfo() {
        APIGetWebSiteInfoResponse response = homeService.getWebSiteInfoV2();
        return ResponseHelper.ok(response);
    }

    @GetMapping
    @Operation(summary = "Get home data", method = "GET")
    @ApiResponse(responseCode = "200", description = "Get home data")
    public Response<APIGetHomeInfoResponse> getHomeData() {
        APIGetHomeInfoResponse response = homeService.getHomeData();
        return ResponseHelper.ok(response);
    }

    @GetMapping("/hot")
    @Operation(summary = "Get hot news", method = "GET")
    @ApiResponse(responseCode = "200", description = "Get hot news")
    public Response<String> hotNews(@RequestParam(name = "type", required = false) String type) {
        String hot = homeService.hotNews(type);
        return ResponseHelper.ok(hot);
    }
}

