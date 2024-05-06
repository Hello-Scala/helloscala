package com.helloscala.controller;


import com.helloscala.annotation.BusinessLogger;
import com.helloscala.common.ResponseResult;
import com.helloscala.entity.Message;
import com.helloscala.service.ApiMessageService;
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
@Tag(name = "评论留言接口")
@RequiredArgsConstructor
public class
ApiMessageController {

    private final ApiMessageService messageService;

    @BusinessLogger(value = "留言模块-留言列表",type = "查询",desc = "留言列表")
    @RequestMapping(value = "/",method = RequestMethod.GET)
    @Operation(summary = "留言列表", method = "GET")
    @ApiResponse(responseCode = "200", description = "留言列表")
    public ResponseResult selectMessageList(){
        return messageService.selectMessageList();
    }


    @BusinessLogger(value = "留言模块-用户留言",type = "添加",desc = "用户留言")
    @RequestMapping(value = "/",method = RequestMethod.POST)
    @Operation(summary = "添加留言", method = "POST")
    @ApiResponse(responseCode = "200", description = "添加留言")
    public ResponseResult addMessage(@RequestBody Message message){
        return messageService.addMessage(message);
    }

}

