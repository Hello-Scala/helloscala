package com.helloscala.admin.controller;


import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helloscala.admin.controller.request.BOCreateDictRequest;
import com.helloscala.admin.controller.request.BOUpdateDictRequest;
import com.helloscala.admin.controller.view.BODictView;
import com.helloscala.admin.service.BODictService;
import com.helloscala.common.annotation.OperationLogger;
import com.helloscala.common.web.response.EmptyResponse;
import com.helloscala.common.web.response.Response;
import com.helloscala.common.web.response.ResponseHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;

@RestController
@RequestMapping("/system/dict")
@Tag(name = "Dict management")
@RequiredArgsConstructor
public class DictController {

    private final BODictService dictService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @Operation(summary = "List dict types", method = "GET")
    @ApiResponse(responseCode = "200", description = "List dict types")
    public Response<Page<BODictView>> selectDictPage(@RequestParam(name = "name", required = false) String name,
                                                     @RequestParam(name = "status", required = false) Integer status) {
        Page<BODictView> dictPage = dictService.listDictByPage(name, String.valueOf(status));
        return ResponseHelper.ok(dictPage);
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @SaCheckPermission("system:dict:add")
    @Operation(summary = "Add dict", method = "POST")
    @ApiResponse(responseCode = "200", description = "Add dict")
    @OperationLogger(value = "Add dict")
    public EmptyResponse insert(@RequestBody BOCreateDictRequest request) {
        String userId = StpUtil.getLoginIdAsString();
        dictService.create(userId, request);
        return ResponseHelper.ok();
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    @SaCheckPermission("system:dict:update")
    @Operation(summary = "U", method = "PUT")
    @ApiResponse(responseCode = "200", description = "Update dict")
    @OperationLogger(value = "Update dict")
    public EmptyResponse update(@RequestBody BOUpdateDictRequest request) {
        String userId = StpUtil.getLoginIdAsString();
        dictService.update(userId, request);
        return ResponseHelper.ok();
    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    @SaCheckPermission("system:dict:delete")
    @Operation(summary = "Batch delete", method = "DELETE")
    @ApiResponse(responseCode = "200", description = "Batch delete")
    @OperationLogger(value = "Batch delete")
    public EmptyResponse deleteBatch(@RequestBody List<String> ids) {
        String userId = StpUtil.getLoginIdAsString();
        dictService.deleteDictData(userId, new HashSet<>(ids));
        return ResponseHelper.ok();
    }
}

