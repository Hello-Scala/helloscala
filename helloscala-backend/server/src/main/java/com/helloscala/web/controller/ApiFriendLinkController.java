package com.helloscala.web.controller;


import com.helloscala.common.annotation.BusinessLogger;
import com.helloscala.common.ResponseResult;
import com.helloscala.common.entity.FriendLink;
import com.helloscala.web.service.ApiFriendLinkService;
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
@Tag(name = "Friend link API-V1")
@RequiredArgsConstructor
public class ApiFriendLinkController {

    private final ApiFriendLinkService friendLinkService;

    @BusinessLogger(value = "Friend link - website",type = "search",desc = "list links")
    @RequestMapping(value = "/",method = RequestMethod.GET)
    @Operation(summary = "List links", method = "GET")
    @ApiResponse(responseCode = "200", description = "List links")
    public ResponseResult selectFriendLinkList(){
        return friendLinkService.selectFriendLinkList();
    }

    @BusinessLogger(value = "Friend link apply add",type = "add",desc = "Apply to add link")
    @RequestMapping(value = "/",method = RequestMethod.POST)
    @Operation(summary = "Apply to add link", method = "POST")
    @ApiResponse(responseCode = "200", description = "Apply to add link")
    public ResponseResult addFriendLink(@RequestBody FriendLink friendLink){
        return friendLinkService.addFriendLink(friendLink);
    }
}

