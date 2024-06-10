package com.helloscala.admin.controller;


import cn.dev33.satoken.annotation.SaCheckPermission;
import com.helloscala.common.annotation.OperationLogger;
import com.helloscala.common.ResponseResult;
import com.helloscala.common.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/system/message")
@Tag(name = "Message managenment")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @RequestMapping(value="/list",method = RequestMethod.GET)
    @Operation(summary = "List messages", method = "GET")
    @ApiResponse(responseCode = "200", description = "List messages")
    public ResponseResult selectMessagePage(@RequestParam(name = "name", required = false) String name){
        return messageService.selectMessagePage(name);
    }

    @RequestMapping(value = "/delete",method = RequestMethod.DELETE)
    @SaCheckPermission("system:message:delete")
    @OperationLogger(value = "Batch delete messages")
    @Operation(summary = "Batch delete messages", method = "DELETE")
    @ApiResponse(responseCode = "200", description = "Batch delete messages")
    public ResponseResult deleteBatch(@RequestBody List<Integer> ids){
        return messageService.deleteMessage(ids);
    }
}

