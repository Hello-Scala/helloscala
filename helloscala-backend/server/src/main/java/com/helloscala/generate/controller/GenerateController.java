package com.helloscala.generate.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helloscala.common.vo.system.TableVO;
import com.helloscala.common.web.response.Response;
import com.helloscala.common.web.response.ResponseHelper;
import com.helloscala.generate.service.GenerateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;


@RestController
@RequiredArgsConstructor
@RequestMapping("/system/generate")
public class GenerateController {

    private final GenerateService generateService;


    @GetMapping(value = "/list")
    public Response<Page<TableVO>> list() throws IOException {
        Page<TableVO> tableVOPage = generateService.selectListTables();
        return ResponseHelper.ok(tableVOPage);
    }

    @SaCheckPermission("system:generate:preview")
    @GetMapping(value = "/preview/{tableName}")
    public Response<Map<String, String>> index(@PathVariable(value = "tableName") String tableName) throws IOException {
        Map<String, String> preview = generateService.preview(tableName);
        return ResponseHelper.ok(preview);
    }

    @GetMapping("/download")
    @SaCheckPermission("system:generate:download")
    public void download(@RequestParam(name = "tableName", required = true) String tableName,
                         @RequestParam(name = "downloadPath", required = true) String downloadPath) throws IOException {
        generateService.download(tableName, downloadPath);
    }
}
