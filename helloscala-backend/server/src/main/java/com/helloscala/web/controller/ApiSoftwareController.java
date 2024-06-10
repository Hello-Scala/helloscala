package com.helloscala.web.controller;


import com.helloscala.common.annotation.BusinessLogger;
import com.helloscala.common.ResponseResult;
import com.helloscala.web.service.ApiSoftwareService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/software")
@Tag(name = "Software management")
@RequiredArgsConstructor
public class ApiSoftwareController {

    private final ApiSoftwareService softwareService;

    @BusinessLogger(value = "List opensource software",type = "search",desc = "List opensource software")
    @RequestMapping(value = "/",method = RequestMethod.GET)
    @Operation(summary = "List opensource software", method = "GET")
    @ApiResponse(responseCode = "200", description = "List opensource software")
    public ResponseResult selectSoftwareList(){
        return softwareService.selectSoftwareList();
    }
}
