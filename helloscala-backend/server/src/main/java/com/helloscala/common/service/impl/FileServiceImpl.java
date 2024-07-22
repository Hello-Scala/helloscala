package com.helloscala.common.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import com.helloscala.common.Constants;
import com.helloscala.common.entity.Resource;
import com.helloscala.common.enums.DataEventEnum;
import com.helloscala.common.enums.FileUploadModelEnum;
import com.helloscala.common.event.DataEventPublisherService;
import com.helloscala.common.service.FileService;
import com.helloscala.common.service.SystemConfigService;
import com.helloscala.common.strategy.context.FileUploadStrategyContext;
import com.helloscala.common.web.exception.BadRequestException;
import com.helloscala.common.web.exception.FailedDependencyException;
import jakarta.servlet.ServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;


@Slf4j
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
    public String upload(MultipartFile file) {
        if (file.getSize() > getMaxFileSize().toBytes()) {
            throw new BadRequestException("文件大小不能大于10M");
        }
        String suffix = Objects.requireNonNull(file.getOriginalFilename()).substring(file.getOriginalFilename().lastIndexOf(".") + 1);
        if (!Constants.FIELD_SUFFIX.toUpperCase().contains(suffix.toUpperCase())) {
            throw new BadRequestException("请选择jpg,jpeg,gif,png,mp4格式的图片");
        }
        getFileUploadWay();
        String key = fileUploadStrategyContext.executeUpload(strategy.getStrategy(), file, suffix);

        Resource resource = Resource.builder().url(key).type(suffix).platform(strategy.getDesc()).userId(StpUtil.getLoginIdAsString()).build();
        dataEventPublisherService.publishData(DataEventEnum.RESOURCE_ADD, resource);
        log.info("Upload file success, key={}!", key);
        return key;
    }

    @Override
    public void download(String url, ServletResponse response) {
        getFileUploadWay();
        fileUploadStrategyContext.executeDownload(strategy.getStrategy(), url, response);
    }

    @Override
    public void delBatchFile(String... key) {
        getFileUploadWay();
        Boolean isSuccess = fileUploadStrategyContext.executeDelete(strategy.getStrategy(), key);
        if (!isSuccess) {
            throw new FailedDependencyException("Delete file failed, keys=[{}]!", StrUtil.join(",", ListUtil.of(key)));
        }
    }

    private void getFileUploadWay() {
        if (strategy == null) {
            strategy = FileUploadModelEnum.getStrategy(systemConfigService.getCustomizeOne().getFileUploadWay());
        }
    }
}
