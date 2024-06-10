package com.helloscala.web.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.helloscala.common.annotation.AccessLimit;
import com.helloscala.common.annotation.BusinessLogger;
import com.helloscala.common.ResponseResult;
import com.helloscala.web.service.ApiSignService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Sign-API")
@RequestMapping("v1/sign")
@RequiredArgsConstructor
public class ApiSignController {

    private final ApiSignService apiSignService;

    @SaCheckLogin
    @RequestMapping(value = "getSignRecords",method = RequestMethod.GET)
    @Operation(summary = "Get user sign records", method = "GET")
    @ApiResponse(responseCode = "200", description = "Get user sign records")
    public ResponseResult getSignRecords(@RequestParam(name = "startTime", required = false) String startTime,
                                         @RequestParam(name = "endTime", required = false) String endTime){
        return apiSignService.getSignRecords(startTime,endTime);
    }

    @AccessLimit
    @SaCheckLogin
    @RequestMapping(value = "/",method = RequestMethod.GET)
    @BusinessLogger(value = "User sign in",type = "add",desc = "User sign in")
    @Operation(summary = "User sign in", method = "GET")
    @ApiResponse(responseCode = "200", description = "User sign in")
    public ResponseResult sign(@RequestParam(name = "time", required = true) String time){
        return apiSignService.sign(time);
    }

    @SaCheckLogin
    @RequestMapping(value = "validateTodayIsSign",method = RequestMethod.GET)
    @Operation(summary = "Check user sign in", method = "GET")
    @ApiResponse(responseCode = "200", description = "Check user sign in")
    public ResponseResult validateTodayIsSign(){
        return apiSignService.validateTodayIsSign();
    }
}
