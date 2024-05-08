package com.helloscala.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.helloscala.common.ResponseResult;
import com.helloscala.entity.Say;
import com.helloscala.service.ApiSayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "说说接口-API")
@RequestMapping("v1/say")
@RequiredArgsConstructor
public class ApiSayController {

    private final ApiSayService apiSayService;

    @RequestMapping(value = "getSayList",method = RequestMethod.GET)
    @Operation(summary = "说说列表", method = "GET")
    @ApiResponse(responseCode = "200", description = "说说列表")
    public ResponseResult selectSayList(){
        return apiSayService.selectSayList();
    }


    @SaCheckLogin
    @RequestMapping(value = "insertSay",method = RequestMethod.POST)
    @Operation(summary = "添加说说说", method = "POST")
    @ApiResponse(responseCode = "200", description = "添加说说说")
    public ResponseResult insertSay(@RequestBody Say say){
        return apiSayService.insertSay(say);
    }

}
