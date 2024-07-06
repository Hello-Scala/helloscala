package com.helloscala.web.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helloscala.common.entity.Say;
import com.helloscala.common.vo.say.ApiSayVO;
import com.helloscala.common.web.response.EmptyResponse;
import com.helloscala.common.web.response.Response;
import com.helloscala.common.web.response.ResponseHelper;
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
    public Response<Page<ApiSayVO>> selectSayList(){
        Page<ApiSayVO> apiSayVOPage = apiSayService.selectSayList();
        return ResponseHelper.ok(apiSayVOPage);
    }


    @SaCheckLogin
    @RequestMapping(value = "insertSay",method = RequestMethod.POST)
    @Operation(summary = "Add says", method = "POST")
    @ApiResponse(responseCode = "200", description = "Add says")
    public EmptyResponse insertSay(@RequestBody Say say){
        apiSayService.insertSay(say);
        return ResponseHelper.ok();
    }

}
