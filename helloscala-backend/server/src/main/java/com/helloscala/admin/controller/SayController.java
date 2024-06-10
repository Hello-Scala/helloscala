package com.helloscala.admin.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.helloscala.common.annotation.OperationLogger;
import com.helloscala.common.ResponseResult;
import com.helloscala.common.entity.Say;
import com.helloscala.common.service.SayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// todo name
@RestController
@RequestMapping("/system/say")
@Tag(name = "Says")
@RequiredArgsConstructor
public class SayController {

    private final SayService sayService;

    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @Operation(summary = "List says", method = "GET")
    @ApiResponse(responseCode = "200", description = "List says")
    public ResponseResult selectSayPage(@RequestParam(name = "keywords", required = false) String keywords){
        return sayService.selectSayPage(keywords);
    }

    @RequestMapping(value = "/info",method = RequestMethod.GET)
    @Operation(summary = "Get say detail", method = "GET")
    @ApiResponse(responseCode = "200", description = "Get say detail")
    public ResponseResult info(@RequestParam(name = "id", required = true) String id){
        return sayService.selectSayById(id);
    }

    @OperationLogger(value = "Update says")
    @SaCheckPermission("system:say:update")
    @RequestMapping(value = "/update",method = RequestMethod.PUT)
    @Operation(summary = "Update says", method = "PUT")
    @ApiResponse(responseCode = "200", description = "Update says")
    public ResponseResult updateSay(@RequestBody Say say){
        return sayService.updateSay(say);
    }

    @OperationLogger(value = "Publish a say")
    @SaCheckPermission("system:say:add")
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    @Operation(summary = "Publish a say", method = "POST")
    @ApiResponse(responseCode = "200", description = "发表说说")
    public ResponseResult addSay(@RequestBody Say say){
        return sayService.addSay(say);
    }

    @OperationLogger(value = "Delete say")
    @SaCheckPermission("system:say:delete")
    @RequestMapping(value = "/delete",method = RequestMethod.DELETE)
    @Operation(summary = "Delete say", method = "DELETE")
    @ApiResponse(responseCode = "200", description = "Delete say")
    public ResponseResult deleteSay(@RequestBody List<String> ids){
        return sayService.deleteSay(ids);
    }
}
