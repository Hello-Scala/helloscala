package com.helloscala.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.helloscala.annotation.OperationLogger;
import com.helloscala.common.ResponseResult;
import com.helloscala.entity.Say;
import com.helloscala.service.SayService;
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
@RequestMapping("/system/say")
@Tag(name = "说说管理")
@RequiredArgsConstructor
public class SayController {

    private final SayService sayService;

    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @Operation(summary = "说说列表", method = "GET")
    @ApiResponse(responseCode = "200", description = "说说列表")
    public ResponseResult selectSayPage(@RequestParam(name = "keywords", required = false) String keywords){
        return sayService.selectSayPage(keywords);
    }

    @RequestMapping(value = "/info",method = RequestMethod.GET)
    @Operation(summary = "说说详情", method = "GET")
    @ApiResponse(responseCode = "200", description = "说说详情")
    public ResponseResult info(@RequestParam(name = "id", required = true) String id){
        return sayService.selectSayById(id);
    }

    @OperationLogger(value = "修改说说")
    @SaCheckPermission("system:say:update")
    @RequestMapping(value = "/update",method = RequestMethod.PUT)
    @Operation(summary = "修改说说", method = "PUT")
    @ApiResponse(responseCode = "200", description = "修改说说")
    public ResponseResult updateSay(@RequestBody Say say){
        return sayService.updateSay(say);
    }

    @OperationLogger(value = "发表说说")
    @SaCheckPermission("system:say:add")
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    @Operation(summary = "发表说说", method = "POST")
    @ApiResponse(responseCode = "200", description = "发表说说")
    public ResponseResult addSay(@RequestBody Say say){
        return sayService.addSay(say);
    }

    @OperationLogger(value = "删除说说")
    @SaCheckPermission("system:say:delete")
    @RequestMapping(value = "/delete",method = RequestMethod.DELETE)
    @Operation(summary = "删除说说", method = "DELETE")
    @ApiResponse(responseCode = "200", description = "删除说说")
    public ResponseResult deleteSay(@RequestBody List<String> ids){
        return sayService.deleteSay(ids);
    }

}
