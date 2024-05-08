package com.helloscala.controller;


import cn.dev33.satoken.annotation.SaCheckPermission;
import com.helloscala.annotation.OperationLogger;
import com.helloscala.common.ResponseResult;
import com.helloscala.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/system/message")
@Tag(name = "留言管理-接口")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @RequestMapping(value="/list",method = RequestMethod.GET)
    @Operation(summary = "留言列表", method = "GET")
    @ApiResponse(responseCode = "200", description = "留言列表")
    public ResponseResult selectMessagePage(@RequestParam(name = "name", required = false) String name){
        return messageService.selectMessagePage(name);
    }

    @RequestMapping(value = "/delete",method = RequestMethod.DELETE)
    @SaCheckPermission("system:message:delete")
    @OperationLogger(value = "批量删除留言")
    @Operation(summary = "批量删除留言", method = "DELETE")
    @ApiResponse(responseCode = "200", description = "批量删除留言")
    public ResponseResult deleteBatch(@RequestBody List<Integer> ids){
        return messageService.deleteMessage(ids);
    }
}

