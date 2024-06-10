package com.helloscala.generate.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.helloscala.common.ResponseResult;
import com.helloscala.generate.service.GenerateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@RestController
@RequiredArgsConstructor
@RequestMapping("/system/generate")
public class GenerateController {

    private final GenerateService generateService;


    @GetMapping(value = "/list")
    public ResponseResult list() throws IOException {
        return generateService.selectListTables();
    }

    @SaCheckPermission("system:generate:preview")
    @GetMapping(value = "/preview/{tableName}")
    public ResponseResult index(@PathVariable(value = "tableName") String tableName) throws IOException {
        return generateService.preview(tableName);
    }

    @GetMapping("/download")
    @SaCheckPermission("system:generate:download")
    public void download(@RequestParam(name = "tableName", required = true) String tableName,
                         @RequestParam(name = "downloadPath", required = true) String downloadPath) throws IOException {
        generateService.download(tableName, downloadPath);
    }
}
