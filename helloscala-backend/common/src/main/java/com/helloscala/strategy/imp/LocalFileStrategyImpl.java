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

    private final Logger logger = LoggerFactory.getLogger(LocalFileStrategyImpl.class);

    private final SystemConfigService systemConfigService;
    @Value("${file.upload-folder}")
    private String UPLOAD_FOLDER;

    private final FileStorageService service;

    private String platform = "local-1";

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
        logger.info("------初始化本地上传配置文件成功-----");
    }


    /**
     * 上传文件
     * @param file 文件
     * @param suffix 后缀
     * @return 文件地址
     */
    @Override
    public String upload(MultipartFile file,String suffix) {
        String path = DateUtil.dateTimeToStr(DateUtil.getNowDate(), DateUtil.YYYYMMDD)  + "/";
        return service.of(file).setPath(path).setPlatform(platform).setSaveFilename(file.getOriginalFilename()).upload().getUrl();
    }

    /**
     * 删除文件
     * @param keys 文件名
     * @return
     */
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

    /**
     * 递归查询文件
     * @param file
     * @param fileNames
     * @return
     */
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
