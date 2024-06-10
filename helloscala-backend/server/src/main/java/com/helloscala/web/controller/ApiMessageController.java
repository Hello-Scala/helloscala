package com.helloscala.web.controller;


import com.helloscala.common.annotation.BusinessLogger;
import com.helloscala.common.ResponseResult;
import com.helloscala.common.entity.Message;
import com.helloscala.web.service.ApiMessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/v1/message")
@Tag(name = "Message management")
@RequiredArgsConstructor
public class
ApiMessageController {

    private final ApiMessageService messageService;

    @BusinessLogger(value = "Website list message",type = "search",desc = "Website list message")
    @RequestMapping(value = "/",method = RequestMethod.GET)
    @Operation(summary = "Website list message", method = "GET")
    @ApiResponse(responseCode = "200", description = "Website list message")
    public ResponseResult selectMessageList(){
        return messageService.selectMessageList();
    }


    @BusinessLogger(value = "Website add message",type = "add",desc = "add message")
    @RequestMapping(value = "/",method = RequestMethod.POST)
    @Operation(summary = "Add message", method = "POST")
    @ApiResponse(responseCode = "200", description = "Add message")
    public ResponseResult addMessage(@RequestBody Message message){
        return messageService.addMessage(message);
    }
}

