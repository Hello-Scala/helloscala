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

@Service("aliFileStrategyImpl")
@RequiredArgsConstructor
public class AliFileStrategyImpl implements FileStrategy {

    private final Logger logger = LoggerFactory.getLogger(AliFileStrategyImpl.class);

    private final SystemConfigService systemConfigService;

    private final FileStorageService service;
    private final String platform = "ali-1";


    @PostConstruct
    private void init(){
        SystemConfig systemConfig = systemConfigService.getCustomizeOne();

        FileStorageProperties.AliyunOssConfig config = new FileStorageProperties.AliyunOssConfig();
        config.setPlatform(platform);
        config.setAccessKey(systemConfig.getQiNiuAccessKey());
        config.setSecretKey(systemConfig.getQiNiuSecretKey());
        config.setDomain(systemConfig.getQiNiuPictureBaseUrl());
        config.setBucketName(systemConfig.getQiNiuBucket());

        service.getFileStorageList().addAll(FileStorageServiceBuilder.buildAliyunOssFileStorage(Collections.singletonList(config),null));

        logger.info("------初始化阿里云oss上传配置文件成功-----");
    }


    @Override
    public String upload(MultipartFile file,String suffix) {
        String path = DateUtil.dateTimeToStr(DateUtil.getNowDate(), DateUtil.YYYYMMDD)  + "/";
        return service.of(file).setPath(path).setPlatform(platform).setSaveFilename(file.getOriginalFilename()).upload().getUrl();
    }
    /**
     * 删除文件 -- 阿里云OSS
     *
     * @param key   文件url
     * @return      ResponseResult
     */
    @Override
    public Boolean delete(String ...key) {
      return false;
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
