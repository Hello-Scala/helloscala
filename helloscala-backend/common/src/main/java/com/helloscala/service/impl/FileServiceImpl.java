package com.helloscala.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import com.helloscala.common.Constants;
import com.helloscala.common.ResponseResult;
import com.helloscala.entity.Resource;
import com.helloscala.enums.DataEventEnum;
import com.helloscala.enums.FileUploadModelEnum;
import com.helloscala.event.DataEventPublisherService;
import com.helloscala.exception.BusinessException;
import com.helloscala.service.FileService;
import com.helloscala.service.SystemConfigService;
import com.helloscala.strategy.context.FileUploadStrategyContext;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.ServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;


@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
    @Value(value = "${spring.servlet.multipart.max-file-size:5MB}")
    private String maxFileSizeStr;

    private final SystemConfigService systemConfigService;

    private final FileUploadStrategyContext fileUploadStrategyContext;

    private FileUploadModelEnum strategy;

    private final DataEventPublisherService dataEventPublisherService;


    private DataSize getMaxFileSize() {
        if (maxFileSizeStr != null) {
            return DataSize.parse(maxFileSizeStr);
        } else {
            return DataSize.ofMegabytes(5);
        }
    }

    /**
     * 上传文件
     *
     * @param file
     * @return
     */
    @Override
    public ResponseResult upload(MultipartFile file) {
        if (file.getSize() > getMaxFileSize().toBytes()) {
            return ResponseResult.error("文件大小不能大于10M");
        }
        //获取文件后缀
        String suffix = Objects.requireNonNull(file.getOriginalFilename()).substring(file.getOriginalFilename().lastIndexOf(".") + 1);
        if (!Constants.FIELD_SUFFIX.toUpperCase().contains(suffix.toUpperCase())) {
            return ResponseResult.error("请选择jpg,jpeg,gif,png,mp4格式的图片");
        }
        getFileUploadWay();
        String key = fileUploadStrategyContext.executeUpload(strategy.getStrategy(), file, suffix);

        Resource resource = Resource.builder().url(key).type(suffix).platform(strategy.getDesc()).userId(StpUtil.getLoginIdAsString()).build();
        dataEventPublisherService.publishData(DataEventEnum.RESOURCE_ADD, resource);
        return ResponseResult.success(HttpStatus.CREATED.value(), "文件上传成功", key);
    }

    @Override
    public void download(String url, ServletResponse response) {
        getFileUploadWay();
        fileUploadStrategyContext.executeDownload(strategy.getStrategy(), url, response);
    }

    /**
     * 删除文件
     *
     * @param key
     * @return
     */
    @Override
    public ResponseResult delBatchFile(String... key) {
        getFileUploadWay();
        Boolean isSuccess = fileUploadStrategyContext.executeDelete(strategy.getStrategy(), key);
        if (!isSuccess) {
            throw new BusinessException("Delete file failed, keys=[{}]!", StrUtil.join(",", ListUtil.of(key)));
        }
        return ResponseResult.success();
    }

    private void getFileUploadWay() {
        if (strategy == null) {
            strategy = FileUploadModelEnum.getStrategy(systemConfigService.getCustomizeOne().getFileUploadWay());
        }
    }
}
