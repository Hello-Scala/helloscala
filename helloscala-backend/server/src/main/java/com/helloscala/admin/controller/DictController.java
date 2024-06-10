package com.helloscala.admin.controller;


import cn.dev33.satoken.annotation.SaCheckPermission;
import com.helloscala.common.annotation.OperationLogger;
import com.helloscala.common.ResponseResult;
import com.helloscala.common.entity.Dict;
import com.helloscala.common.service.DictService;
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
    public ResponseResult selectDictPage(@RequestParam(name = "name", required = false) String name,
                                         @RequestParam(name = "status", required = false) Integer status){
        return dictService.selectDictPage(name,status);
    }

    @RequestMapping(value = "/add",method = RequestMethod.POST)
    @SaCheckPermission("system:dict:add")
    @Operation(summary = "Add dict", method = "POST")
    @ApiResponse(responseCode = "200", description = "Add dict")
    @OperationLogger(value = "Add dict")
    public ResponseResult insert(@RequestBody Dict dict){
        return dictService.addDict(dict);
    }

    @RequestMapping(value = "/update",method = RequestMethod.PUT)
    @SaCheckPermission("system:dict:update")
    @Operation(summary = "U", method = "PUT")
    @ApiResponse(responseCode = "200", description = "Update dict")
    @OperationLogger(value = "Update dict")
    public ResponseResult update(@RequestBody Dict dict){
        return dictService.updateDict(dict);
    }

    @RequestMapping(value = "/delete",method = RequestMethod.DELETE)
    @SaCheckPermission("system:dict:delete")
    @Operation(summary = "Batch delete", method = "DELETE")
    @ApiResponse(responseCode = "200", description = "Batch delete")
    @OperationLogger(value = "Batch delete")
    public ResponseResult deleteBatch(@RequestBody List<Long> list){
        return dictService.deleteDict(list);
    }
}

