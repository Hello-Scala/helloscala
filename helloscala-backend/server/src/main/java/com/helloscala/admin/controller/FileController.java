package com.helloscala.admin.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.helloscala.common.annotation.OperationLogger;
import com.helloscala.common.ResponseResult;
import com.helloscala.common.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("system/file")
@Tag(name = "File mangement")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;


    @OperationLogger("Upload file")
    @RequestMapping(value = "/upload",method = RequestMethod.POST)
    @Operation(summary = "Upload file", method = "POST")
    @ApiResponse(responseCode = "201", description = "Upload file")
    public ResponseResult upload(@RequestPart("multipartFile") MultipartFile multipartFile){
        return fileService.upload(multipartFile);
    }


    @OperationLogger("Batch delete files")
    @SaCheckPermission("system:file:delete")
    @RequestMapping(value = "/delete",method = RequestMethod.DELETE)
    @Operation(summary = "Batch delete files", method = "DELETE")
    @ApiResponse(responseCode = "200", description = "Batch delete files")
    public ResponseResult delBatchFile(@RequestParam(name = "key", required = true) String key){
        return fileService.delBatchFile(key);
    }
}
