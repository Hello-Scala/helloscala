package com.helloscala.web.controller;


import com.helloscala.common.annotation.BusinessLogger;
import com.helloscala.common.ResponseResult;
import com.helloscala.web.service.ApiTagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/v1/tag")
@Tag(name = "Tage API")
@RequiredArgsConstructor
public class ApiTagsController {

    private final ApiTagService tagsService;

    @BusinessLogger(value = "Website list tags",type = "search",desc = "Website list tags")
    @RequestMapping(value = "/",method = RequestMethod.GET)
    @Operation(summary = "Website list tags", method = "GET")
    @ApiResponse(responseCode = "200", description = "Website list tags")
    public ResponseResult selectTagList(){
        return tagsService.selectTagList();
    }

}

