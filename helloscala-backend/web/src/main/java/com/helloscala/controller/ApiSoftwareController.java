package com.helloscala.controller;


import com.helloscala.annotation.BusinessLogger;
import com.helloscala.common.ResponseResult;
import com.helloscala.service.ApiSoftwareService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/software")
@Tag(name = "开源软件接口")
@RequiredArgsConstructor
public class ApiSoftwareController {

    private final ApiSoftwareService softwareService;

    @BusinessLogger(value = "开源软件-用户访问开源软件",type = "查询",desc = "用户访问页面")
    @RequestMapping(value = "/",method = RequestMethod.GET)
    @Operation(summary = "开源软件", method = "GET")
    @ApiResponse(responseCode = "200", description = "开源软件")
    public ResponseResult selectSoftwareList(){
        return softwareService.selectSoftwareList();
    }
}
