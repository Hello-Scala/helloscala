package com.helloscala.controller;


import cn.dev33.satoken.annotation.SaCheckPermission;
import com.helloscala.annotation.OperationLogger;
import com.helloscala.common.ResponseResult;
import com.helloscala.entity.Dict;
import com.helloscala.service.DictService;
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
@RequestMapping("/system/dict")
@Tag(name = "字典类型管理")
@RequiredArgsConstructor
public class DictController {

    private final DictService dictService;

    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @Operation(summary = "字典类型列表", method = "GET")
    @ApiResponse(responseCode = "200", description = "字典类型列表")
    public ResponseResult selectDictPage(@RequestParam(name = "name", required = false) String name,
                                         @RequestParam(name = "status", required = false) Integer status){
        return dictService.selectDictPage(name,status);
    }

    @RequestMapping(value = "/add",method = RequestMethod.POST)
    @SaCheckPermission("system:dict:add")
    @Operation(summary = "添加字典", method = "POST")
    @ApiResponse(responseCode = "200", description = "添加字典")
    @OperationLogger(value = "添加字典")
    public ResponseResult insert(@RequestBody Dict dict){
        return dictService.addDict(dict);
    }

    @RequestMapping(value = "/update",method = RequestMethod.PUT)
    @SaCheckPermission("system:dict:update")
    @Operation(summary = "修改字典", method = "PUT")
    @ApiResponse(responseCode = "200", description = "修改字典")
    @OperationLogger(value = "修改字典")
    public ResponseResult update(@RequestBody Dict dict){
        return dictService.updateDict(dict);
    }

    @RequestMapping(value = "/delete",method = RequestMethod.DELETE)
    @SaCheckPermission("system:dict:delete")
    @Operation(summary = "批量删除字典", method = "DELETE")
    @ApiResponse(responseCode = "200", description = "批量删除字典")
    @OperationLogger(value = "批量删除字典")
    public ResponseResult deleteBatch(@RequestBody List<Long> list){
        return dictService.deleteDict(list);
    }
}

