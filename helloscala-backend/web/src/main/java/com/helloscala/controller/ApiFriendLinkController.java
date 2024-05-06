package com.helloscala.controller;


import com.helloscala.annotation.BusinessLogger;
import com.helloscala.common.ResponseResult;
import com.helloscala.entity.FriendLink;
import com.helloscala.service.ApiFriendLinkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/v1/link")
@Tag(name = "友情链接API-V1")
@RequiredArgsConstructor
public class ApiFriendLinkController {

    private final ApiFriendLinkService friendLinkService;

    @BusinessLogger(value = "友链模块-用户访问页面",type = "查询",desc = "友链列表")
    @RequestMapping(value = "/",method = RequestMethod.GET)
    @Operation(summary = "友链列表", method = "GET")
    @ApiResponse(responseCode = "200", description = "友链列表")
    public ResponseResult selectFriendLinkList(){
        return friendLinkService.selectFriendLinkList();
    }

    @BusinessLogger(value = "友链模块-用户申请友链",type = "添加",desc = "用户申请友链")
    @RequestMapping(value = "/",method = RequestMethod.POST)
    @Operation(summary = "申请友链", method = "POST")
    @ApiResponse(responseCode = "200", description = "申请友链")
    public ResponseResult addFriendLink(@RequestBody FriendLink friendLink){
        return friendLinkService.addFriendLink(friendLink);
    }



}

