package com.helloscala.admin.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helloscala.common.annotation.OperationLogger;
import com.helloscala.common.entity.Say;
import com.helloscala.common.service.SayService;
import com.helloscala.common.web.response.EmptyResponse;
import com.helloscala.common.web.response.Response;
import com.helloscala.common.web.response.ResponseHelper;
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
    public Response<Page<Say>> selectSayPage(@RequestParam(name = "keywords", required = false) String keywords){
        Page<Say> sayPage = sayService.selectSayPage(keywords);
        return ResponseHelper.ok(sayPage);
    }

    @RequestMapping(value = "/info",method = RequestMethod.GET)
    @Operation(summary = "Get say detail", method = "GET")
    @ApiResponse(responseCode = "200", description = "Get say detail")
    public Response<Say> info(@RequestParam(name = "id", required = true) String id){
        Say say = sayService.selectSayById(id);
        return ResponseHelper.ok(say);
    }

    @OperationLogger(value = "Update says")
    @SaCheckPermission("system:say:update")
    @RequestMapping(value = "/update",method = RequestMethod.PUT)
    @Operation(summary = "Update says", method = "PUT")
    @ApiResponse(responseCode = "200", description = "Update says")
    public EmptyResponse updateSay(@RequestBody Say say){
        sayService.updateSay(say);
        return ResponseHelper.ok();
    }

    @OperationLogger(value = "Publish a say")
    @SaCheckPermission("system:say:add")
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    @Operation(summary = "Publish a say", method = "POST")
    @ApiResponse(responseCode = "200", description = "发表说说")
    public EmptyResponse addSay(@RequestBody Say say){
        sayService.addSay(say);
        return ResponseHelper.ok();
    }

    @OperationLogger(value = "Delete say")
    @SaCheckPermission("system:say:delete")
    @RequestMapping(value = "/delete",method = RequestMethod.DELETE)
    @Operation(summary = "Delete say", method = "DELETE")
    @ApiResponse(responseCode = "200", description = "Delete say")
    public EmptyResponse deleteSay(@RequestBody List<String> ids){
        sayService.deleteSay(ids);
        return ResponseHelper.ok();
    }
}
