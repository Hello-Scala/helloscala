package com.helloscala.common.strategy.imp;

import cn.hutool.core.util.StrUtil;
import com.helloscala.common.config.FtpConfig;
import com.helloscala.common.strategy.FileStrategy;
import com.helloscala.common.utils.DateUtil;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.ServletResponse;
import lombok.RequiredArgsConstructor;
import org.dromara.x.file.storage.core.FileInfo;
import org.dromara.x.file.storage.core.FileStorageProperties;
import org.dromara.x.file.storage.core.FileStorageService;
import org.dromara.x.file.storage.core.FileStorageServiceBuilder;
import org.dromara.x.file.storage.core.platform.FtpFileStorage;
import org.dromara.x.file.storage.core.upload.UploadPretreatment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


@Service("ftpFileStrategyImpl")
@RequiredArgsConstructor
public class FtpFileStrategyImpl implements FileStrategy {
    private final Logger logger = LoggerFactory.getLogger(FtpFileStrategyImpl.class);
    private final FileStorageService service;
    private final FtpConfig ftpConfig;
    private final String platform = "ftp";
    private String baseUrl;

    @PostConstruct
    private void init() {
        FileStorageProperties.FtpConfig config = new FileStorageProperties.FtpConfig();
        config.setPlatform(platform);
        config.setHost(ftpConfig.getHost());
        config.setPort(ftpConfig.getPort());
        config.setUser(ftpConfig.getUsername());
        config.setPassword(ftpConfig.getPassword());
        config.setBasePath(ftpConfig.getBasePath());
        config.setStoragePath("");
        config.setDomain(ftpConfig.getDomain());
        List<FtpFileStorage> ftpFileStorages = FileStorageServiceBuilder.buildFtpFileStorage(Collections.singletonList(config), null);
        service.setFileStorageList(new CopyOnWriteArrayList<>(ftpFileStorages));
        baseUrl = ftpConfig.getDomain();
        logger.info("------init ftp settings success-----");
    }

    @Override
    public String upload(MultipartFile file, String suffix) {
        String path = DateUtil.dateTimeToStr(DateUtil.getNowDate(), DateUtil.YYYYMMDD) + "/";
        UploadPretreatment uploadPretreatment = service.of(file).setPath(path).setPlatform(platform).setSaveFilename(file.getOriginalFilename());
        return uploadPretreatment.upload().getUrl();
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
                .setFilename(StrUtil.removePrefix(key, "/"));
        try {
            service.download(fileInfo).outputStream(response.getOutputStream());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
