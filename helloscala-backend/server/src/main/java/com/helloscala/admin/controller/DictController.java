package com.helloscala.admin.controller;


import cn.dev33.satoken.annotation.SaCheckPermission;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helloscala.common.annotation.OperationLogger;
import com.helloscala.common.entity.Dict;
import com.helloscala.common.service.DictService;
import com.helloscala.common.web.response.EmptyResponse;
import com.helloscala.common.web.response.Response;
import com.helloscala.common.web.response.ResponseHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/system/dict")
@Tag(name = "Dict management")
@RequiredArgsConstructor
public class DictController {

    private final DictService dictService;

    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @Operation(summary = "List dict types", method = "GET")
    @ApiResponse(responseCode = "200", description = "List dict types")
    public Response<Page<Dict>> selectDictPage(@RequestParam(name = "name", required = false) String name,
                                               @RequestParam(name = "status", required = false) Integer status){
        Page<Dict> dictPage = dictService.selectDictPage(name, status);
        return ResponseHelper.ok(dictPage);
    }

    @RequestMapping(value = "/add",method = RequestMethod.POST)
    @SaCheckPermission("system:dict:add")
    @Operation(summary = "Add dict", method = "POST")
    @ApiResponse(responseCode = "200", description = "Add dict")
    @OperationLogger(value = "Add dict")
    public EmptyResponse insert(@RequestBody Dict dict){
        dictService.addDict(dict);
        return ResponseHelper.ok();
    }

    @RequestMapping(value = "/update",method = RequestMethod.PUT)
    @SaCheckPermission("system:dict:update")
    @Operation(summary = "U", method = "PUT")
    @ApiResponse(responseCode = "200", description = "Update dict")
    @OperationLogger(value = "Update dict")
    public EmptyResponse update(@RequestBody Dict dict){
        dictService.updateDict(dict);
        return ResponseHelper.ok();
    }

    @RequestMapping(value = "/delete",method = RequestMethod.DELETE)
    @SaCheckPermission("system:dict:delete")
    @Operation(summary = "Batch delete", method = "DELETE")
    @ApiResponse(responseCode = "200", description = "Batch delete")
    @OperationLogger(value = "Batch delete")
    public EmptyResponse deleteBatch(@RequestBody List<Long> list){
        dictService.deleteDict(list);
        return ResponseHelper.ok();
    }
}

