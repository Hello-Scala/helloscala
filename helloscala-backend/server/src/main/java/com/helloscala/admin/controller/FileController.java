package com.helloscala.admin.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.helloscala.common.annotation.OperationLogger;
import com.helloscala.common.service.FileService;
import com.helloscala.common.web.response.EmptyResponse;
import com.helloscala.common.web.response.Response;
import com.helloscala.common.web.response.ResponseHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("system/file")
@Tag(name = "File management")
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;

    @RequestMapping(value = "/upload",method = RequestMethod.POST)
    @Operation(summary = "Upload file", method = "POST")
    @ApiResponse(responseCode = "201", description = "Upload file")
    public Response<String> upload(@RequestPart("multipartFile") MultipartFile multipartFile){
        String key = fileService.upload(multipartFile);
        return ResponseHelper.ok(key);
    }

    @OperationLogger("Batch delete files")
    @SaCheckPermission("system:file:delete")
    @RequestMapping(value = "/delete",method = RequestMethod.DELETE)
    @Operation(summary = "Batch delete files", method = "DELETE")
    @ApiResponse(responseCode = "200", description = "Batch delete files")
    public EmptyResponse delBatchFile(@RequestParam(name = "key", required = true) String key){
        fileService.delBatchFile(key);
        return ResponseHelper.ok();
    }
}
