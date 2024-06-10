package com.helloscala.common.strategy.imp;

import com.helloscala.common.entity.SystemConfig;
import com.helloscala.common.service.SystemConfigService;
import com.helloscala.common.strategy.FileStrategy;
import com.helloscala.common.strategy.FileUploadStrategy;
import com.helloscala.common.utils.DateUtil;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.ServletResponse;
import lombok.RequiredArgsConstructor;
import org.dromara.x.file.storage.core.FileInfo;
import org.dromara.x.file.storage.core.FileStorageProperties;
import org.dromara.x.file.storage.core.FileStorageService;
import org.dromara.x.file.storage.core.FileStorageServiceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Service("localFileStrategyImpl")
@RequiredArgsConstructor
public class LocalFileStrategyImpl implements FileStrategy {
    private static final String platform = "local-1";
    private final Logger logger = LoggerFactory.getLogger(LocalFileStrategyImpl.class);
    private final SystemConfigService systemConfigService;
    @Value("${file.upload-folder}")
    private String UPLOAD_FOLDER;
    private final FileStorageService service;
    private String baseUrl;

    @PostConstruct
    private void init(){
        SystemConfig systemConfig = systemConfigService.getCustomizeOne();

        FileStorageProperties.LocalPlusConfig config = new FileStorageProperties.LocalPlusConfig();
        config.setPlatform(platform);
        config.setDomain(systemConfig.getLocalFileUrl());
        config.setStoragePath(UPLOAD_FOLDER);
        service.getFileStorageList().addAll(FileStorageServiceBuilder.buildLocalPlusFileStorage(Collections.singletonList(config)));
        baseUrl = systemConfig.getLocalFileUrl();
        logger.info("------Init local file uploading config success-----");
    }


    @Override
    public String upload(MultipartFile file,String suffix) {
        String path = DateUtil.dateTimeToStr(DateUtil.getNowDate(), DateUtil.YYYYMMDD)  + "/";
        return service.of(file).setPath(path).setPlatform(platform).setSaveFilename(file.getOriginalFilename()).upload().getUrl();
    }

    @Override
    public Boolean delete(String... keys) {
        for (String key : keys) {
            String[] str = key.split(baseUrl);
            FileInfo fileInfo = new FileInfo()
                    .setPlatform(platform)
                    .setFilename(str[1]);
            service.delete(fileInfo);
        }
        return true;
    }

    @Override
    public void download(String key, ServletResponse response) {
        FileInfo fileInfo = new FileInfo()
            .setPlatform(platform)
            .setFilename(key);
        try {
            service.download(fileInfo).outputStream(response.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static List<String> getFileNames(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return null;
        }
        List<String> fileNames = new ArrayList<>();
        return getFileNames(file, fileNames);
    }

    private static List<String> getFileNames(File file, List<String> fileNames) {
        File[] files = file.listFiles();
        for (File f : files) {
            if (f.isDirectory()) {
                getFileNames(f, fileNames);
            } else {
                fileNames.add(f.getName());
            }
        }
        return fileNames;
    }
}
