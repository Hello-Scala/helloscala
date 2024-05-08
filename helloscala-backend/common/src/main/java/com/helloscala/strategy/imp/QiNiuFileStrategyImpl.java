package com.helloscala.strategy.imp;

import com.helloscala.entity.SystemConfig;
import com.helloscala.service.SystemConfigService;
import com.helloscala.strategy.FileStrategy;
import com.helloscala.strategy.FileUploadStrategy;
import com.helloscala.utils.DateUtil;
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

    private final Logger logger = LoggerFactory.getLogger(QiNiuFileStrategyImpl.class);

    private final SystemConfigService systemConfigService;

    private final FileStorageService service;


    private final String platform = "qiniu-1";

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
        logger.info("------初始化七牛云上传配置文件成功-----");
    }


    @Override
    public String upload(MultipartFile file,String suffix) {
        String path = DateUtil.dateTimeToStr(DateUtil.getNowDate(), DateUtil.YYYYMMDD)  + "/";
        return service.of(file).setPath(path).setPlatform(platform).setSaveFilename(file.getOriginalFilename()).upload().getUrl();
    }


    /**
     * 批量删除文件
     * @return
     */
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
