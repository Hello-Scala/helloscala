package com.helloscala.admin.controller;


import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helloscala.admin.controller.request.BOCreateDictDataRequest;
import com.helloscala.admin.controller.request.BOUpdateDictDataRequest;
import com.helloscala.admin.controller.view.BODictDataView;
import com.helloscala.admin.controller.view.BOListByTypeDictView;
import com.helloscala.admin.service.BODictDataService;
import com.helloscala.common.annotation.OperationLogger;
import com.helloscala.common.web.response.EmptyResponse;
import com.helloscala.common.web.response.Response;
import com.helloscala.common.web.response.ResponseHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/system/dictData")
@Tag(name = "Dict data management")
@RequiredArgsConstructor
public class DictDataController {

    private final BODictDataService dictDataService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @Operation(summary = "List dict data", method = "GET")
    @ApiResponse(responseCode = "200", description = "List dict data")
    public Response<Page<BODictDataView>> selectDictDataPage(@RequestParam(name = "dictId", required = false) Integer dictId,
                                                             @RequestParam(name = "isPublish", required = false) Integer isPublish) {
        Page<BODictDataView> dictPage = dictDataService.listDictDataByPage(dictId, isPublish);
        return ResponseHelper.ok(dictPage);
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @SaCheckPermission("system:dictData:add")
    @Operation(summary = "Add dict data", method = "POST")
    @ApiResponse(responseCode = "201", description = "Add dict data")
    @OperationLogger(value = "Add dict data")
    public EmptyResponse addDictData(@RequestBody BOCreateDictDataRequest request) {
        String userId = StpUtil.getLoginIdAsString();
        dictDataService.create(userId, request);
        return ResponseHelper.ok();
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    @SaCheckPermission("system:dictData:update")
    @Operation(summary = "Update dict data", method = "PUT")
    @ApiResponse(responseCode = "200", description = "Update dict data")
    @OperationLogger(value = "Update dict data")
    public EmptyResponse update(@RequestBody BOUpdateDictDataRequest request) {
        String userId = StpUtil.getLoginIdAsString();
        dictDataService.update(userId, request);
        return ResponseHelper.ok();
    }

    @RequestMapping(value = "/deleteBatch", method = RequestMethod.DELETE)
    @SaCheckPermission("system:dictData:delete")
    @Operation(summary = "Batch delete dict data", method = "DELETE")
    @ApiResponse(responseCode = "200", description = "Batch delete dict data")
    @OperationLogger(value = "Batch delete dict data")
    public EmptyResponse deleteDictData(@RequestBody List<String> ids) {
        String userId = StpUtil.getLoginIdAsString();
        dictDataService.deleteDictData(userId, ids);
        return ResponseHelper.ok();
    }

    @Deprecated
    @RequestMapping(value = "/getDataByDictType", method = RequestMethod.POST)
    public Response<Map<String, Map<String, Object>>> getDataByDictType(@RequestBody List<String> types) {
        Map<String, Map<String, Object>> dictMap = dictDataService.getDataByDictType(types);
        return ResponseHelper.ok(dictMap);
    }

    @RequestMapping(value = "/listByDictType", method = RequestMethod.POST)
    public Response<List<BOListByTypeDictView>> listByDictType(@RequestBody List<String> types) {
        List<BOListByTypeDictView> dictViews = dictDataService.listByDictType(types);
        return ResponseHelper.ok(dictViews);
    }
}

