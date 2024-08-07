package com.helloscala.common.strategy.imp;

import com.helloscala.common.entity.SystemConfig;
import com.helloscala.common.service.SystemConfigService;
import com.helloscala.common.strategy.FileStrategy;
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
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;


@Service("qiNiuFileStrategyImpl")
@RequiredArgsConstructor
public class QiNiuFileStrategyImpl implements FileStrategy {
    private static final String platform = "qiniu-1";
    private final Logger logger = LoggerFactory.getLogger(QiNiuFileStrategyImpl.class);
    private final SystemConfigService systemConfigService;
    private final FileStorageService service;
    private String baseUrl;

    @PostConstruct
    private void init(){
        SystemConfig systemConfig = systemConfigService.getCustomizeOne();

        FileStorageProperties.QiniuKodoConfig config = new FileStorageProperties.QiniuKodoConfig();
        config.setPlatform(platform);
        config.setAccessKey(systemConfig.getQiNiuAccessKey());
        config.setSecretKey(systemConfig.getQiNiuSecretKey());
        config.setDomain(systemConfig.getQiNiuPictureBaseUrl());
        config.setBucketName(systemConfig.getQiNiuBucket());

        service.getFileStorageList().addAll(FileStorageServiceBuilder.buildQiniuKodoFileStorage(Collections.singletonList(config),null));
        baseUrl = systemConfig.getQiNiuPictureBaseUrl();
        logger.info("------Init qiniu cloud file uploading config success-----");
    }


    @Override
    public String upload(MultipartFile file,String suffix) {
        String path = DateUtil.dateTimeToStr(DateUtil.getNowDate(), DateUtil.YYYYMMDD)  + "/";
        return service.of(file).setPath(path).setPlatform(platform).setSaveFilename(file.getOriginalFilename()).upload().getUrl();
    }


    // todo bulk delete
    @Override
    public Boolean delete(String ...keys) {
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
}
