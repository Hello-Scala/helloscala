package com.helloscala.web.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.helloscala.common.ResponseResult;
import com.helloscala.common.entity.Say;
import com.helloscala.web.service.ApiSayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Says-API")
@RequestMapping("v1/say")
@RequiredArgsConstructor
public class ApiSayController {

    private final ApiSayService apiSayService;

    @RequestMapping(value = "getSayList",method = RequestMethod.GET)
    @Operation(summary = "List says", method = "GET")
    @ApiResponse(responseCode = "200", description = "List says")
    public ResponseResult selectSayList(){
        return apiSayService.selectSayList();
    }


    @SaCheckLogin
    @RequestMapping(value = "insertSay",method = RequestMethod.POST)
    @Operation(summary = "Add says", method = "POST")
    @ApiResponse(responseCode = "200", description = "Add says")
    public ResponseResult insertSay(@RequestBody Say say){
        return apiSayService.insertSay(say);
    }

}
