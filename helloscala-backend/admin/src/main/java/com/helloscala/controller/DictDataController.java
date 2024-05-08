package com.helloscala.controller;


import cn.dev33.satoken.annotation.SaCheckPermission;
import com.helloscala.annotation.OperationLogger;
import com.helloscala.common.ResponseResult;
import com.helloscala.entity.DictData;
import com.helloscala.service.DictDataService;
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
@RequestMapping("/system/dictData")
@Tag(name = "字典数据管理")
@Tag(name = "字典数据管理")
@RequiredArgsConstructor
public class DictDataController {

    private final DictDataService dictDataService;

    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @Operation(summary = "字典数据列表", method = "GET")
    @ApiResponse(responseCode = "200", description = "字典数据列表")
    public ResponseResult selectDictDataPage(@RequestParam(name = "dictId", required = false) Integer dictId,
                                             @RequestParam(name = "isPublish", required = false) Integer isPublish){
        return dictDataService.selectDictDataPage(dictId,isPublish);
    }

    @RequestMapping(value = "/add",method = RequestMethod.POST)
    @SaCheckPermission("system:dictData:add")
    @Operation(summary = "添加字典数据", method = "POST")
    @ApiResponse(responseCode = "201", description = "添加字典数据")
    @OperationLogger(value = "添加字典数据")
    public ResponseResult addDictData(@RequestBody DictData dictData){
        return dictDataService.addDictData(dictData);
    }

    @RequestMapping(value = "/update",method = RequestMethod.PUT)
    @SaCheckPermission("system:dictData:update")
    @Operation(summary = "修改字典数据", method = "PUT")
    @ApiResponse(responseCode = "200", description = "修改字典数据")
    @OperationLogger(value = "修改字典数据")
    public ResponseResult update(@RequestBody DictData dictData){
        return dictDataService.updateDictData(dictData);
    }

    @RequestMapping(value = "/deleteBatch",method = RequestMethod.DELETE)
    @SaCheckPermission("system:dictData:delete")
    @Operation(summary = "批量删除字典数据", method = "DELETE")
    @ApiResponse(responseCode = "200", description = "批量删除字典数据")
    @OperationLogger(value = "批量删除字典数据")
    public ResponseResult deleteDictData(@RequestBody List<Long> ids){
        return dictDataService.deleteDictData(ids);
    }

    @RequestMapping(value = "/getDataByDictType",method = RequestMethod.POST)
    public ResponseResult getDataByDictType(@RequestBody List<String> types){
        return dictDataService.getDataByDictType(types);
    }
}
