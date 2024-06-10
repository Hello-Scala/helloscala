package com.helloscala.admin.controller;


import cn.dev33.satoken.annotation.SaCheckPermission;
import com.helloscala.common.annotation.OperationLogger;
import com.helloscala.common.ResponseResult;
import com.helloscala.common.entity.DictData;
import com.helloscala.common.service.DictDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/system/dictData")
@Tag(name = "Dict data management")
@RequiredArgsConstructor
public class DictDataController {

    private final DictDataService dictDataService;

    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @Operation(summary = "List dict data", method = "GET")
    @ApiResponse(responseCode = "200", description = "List dict data")
    public ResponseResult selectDictDataPage(@RequestParam(name = "dictId", required = false) Integer dictId,
                                             @RequestParam(name = "isPublish", required = false) Integer isPublish){
        return dictDataService.selectDictDataPage(dictId,isPublish);
    }

    @RequestMapping(value = "/add",method = RequestMethod.POST)
    @SaCheckPermission("system:dictData:add")
    @Operation(summary = "Add dict data", method = "POST")
    @ApiResponse(responseCode = "201", description = "Add dict data")
    @OperationLogger(value = "Add dict data")
    public ResponseResult addDictData(@RequestBody DictData dictData){
        return dictDataService.addDictData(dictData);
    }

    @RequestMapping(value = "/update",method = RequestMethod.PUT)
    @SaCheckPermission("system:dictData:update")
    @Operation(summary = "Update dict data", method = "PUT")
    @ApiResponse(responseCode = "200", description = "Update dict data")
    @OperationLogger(value = "Update dict data")
    public ResponseResult update(@RequestBody DictData dictData){
        return dictDataService.updateDictData(dictData);
    }

    @RequestMapping(value = "/deleteBatch",method = RequestMethod.DELETE)
    @SaCheckPermission("system:dictData:delete")
    @Operation(summary = "Batch delete dict data", method = "DELETE")
    @ApiResponse(responseCode = "200", description = "Batch delete dict data")
    @OperationLogger(value = "Batch delete dict data")
    public ResponseResult deleteDictData(@RequestBody List<Long> ids){
        return dictDataService.deleteDictData(ids);
    }

    @RequestMapping(value = "/getDataByDictType",method = RequestMethod.POST)
    public ResponseResult getDataByDictType(@RequestBody List<String> types){
        return dictDataService.getDataByDictType(types);
    }
}

