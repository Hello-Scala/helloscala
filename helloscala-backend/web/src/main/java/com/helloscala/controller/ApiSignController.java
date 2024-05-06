package com.helloscala.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.helloscala.annotation.AccessLimit;
import com.helloscala.annotation.BusinessLogger;
import com.helloscala.common.ResponseResult;
import com.helloscala.service.ApiSignService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "签到接口-API")
@RequestMapping("v1/sign")
@RequiredArgsConstructor
public class ApiSignController {

    private final ApiSignService apiSignService;

    @SaCheckLogin
    @RequestMapping(value = "getSignRecords",method = RequestMethod.GET)
    @Operation(summary = "用户签到记录", method = "GET")
    @ApiResponse(responseCode = "200", description = "用户签到记录")
    public ResponseResult getSignRecords(@RequestParam(name = "startTime", required = false) String startTime,
                                         @RequestParam(name = "endTime", required = false) String endTime){
        return apiSignService.getSignRecords(startTime,endTime);
    }

    @AccessLimit
    @SaCheckLogin
    @RequestMapping(value = "/",method = RequestMethod.GET)
    @BusinessLogger(value = "签到-用户签到",type = "添加",desc = "用户签到")
    @Operation(summary = "用户签到", method = "GET")
    @ApiResponse(responseCode = "200", description = "用户签到")
    public ResponseResult sign(@RequestParam(name = "time", required = true) String time){
        return apiSignService.sign(time);
    }

    @SaCheckLogin
    @RequestMapping(value = "validateTodayIsSign",method = RequestMethod.GET)
    @Operation(summary = "验证用户当日是否签到", method = "GET")
    @ApiResponse(responseCode = "200", description = "验证用户当日是否签到")
    public ResponseResult validateTodayIsSign(){
        return apiSignService.validateTodayIsSign();
    }

}
