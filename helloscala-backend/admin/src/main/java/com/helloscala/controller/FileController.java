package com.helloscala.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.helloscala.annotation.OperationLogger;
import com.helloscala.common.ResponseResult;
import com.helloscala.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("system/file")
@Tag(name = "图片上传-接口")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;


    @RequestMapping(value = "/upload",method = RequestMethod.POST)
    @Operation(summary = "上传文件", method = "POST")
    @ApiResponse(responseCode = "201", description = "上传文件")
    public ResponseResult upload(@RequestPart("multipartFile") MultipartFile multipartFile){
        return fileService.upload(multipartFile);
    }


    @OperationLogger("删除文件")
    @SaCheckPermission("system:file:delete")
    @RequestMapping(value = "/delete",method = RequestMethod.DELETE)
    @Operation(summary = "批量删除文件", method = "DELETE")
    @ApiResponse(responseCode = "200", description = "批量删除文件")
    public ResponseResult delBatchFile(@RequestParam(name = "key", required = true) String key){
        return fileService.delBatchFile(key);
    }
}
