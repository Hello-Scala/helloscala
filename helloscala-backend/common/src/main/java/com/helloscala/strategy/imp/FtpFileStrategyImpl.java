package com.helloscala.strategy.imp;

import com.helloscala.config.FtpConfig;
import com.helloscala.entity.SystemConfig;
import com.helloscala.service.SystemConfigService;
import com.helloscala.strategy.FileStrategy;
import com.helloscala.utils.DateUtil;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.ServletResponse;
import lombok.RequiredArgsConstructor;
import org.dromara.x.file.storage.core.FileInfo;
import org.dromara.x.file.storage.core.FileStorageProperties;
import org.dromara.x.file.storage.core.FileStorageService;
import org.dromara.x.file.storage.core.FileStorageServiceBuilder;
import org.dromara.x.file.storage.core.platform.FtpFileStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;


@Service("ftpFileStrategyImpl")
@RequiredArgsConstructor
public class FtpFileStrategyImpl implements FileStrategy {

    private final Logger logger = LoggerFactory.getLogger(FtpFileStrategyImpl.class);

    private final SystemConfigService systemConfigService;

    private final FileStorageService service;
    private final FtpConfig ftpConfig;

    private final String platform = "ftp";

    private String baseUrl;

    @PostConstruct
    private void init(){
        SystemConfig systemConfig = systemConfigService.getCustomizeOne();

        FileStorageProperties.FtpConfig config = new FileStorageProperties.FtpConfig();
        config.setPlatform(platform);
        config.setHost(ftpConfig.getHost());
        config.setPort(ftpConfig.getPort());
        config.setUser(ftpConfig.getUsername());
        config.setPassword(ftpConfig.getPassword());
        config.setBasePath(ftpConfig.getBasePath());

        config.setDomain(ftpConfig.getDomain());
        List<FtpFileStorage> ftpFileStorages = FileStorageServiceBuilder.buildFtpFileStorage(Collections.singletonList(config), null);
        service.getFileStorageList().addAll(ftpFileStorages);

        baseUrl = systemConfig.getQiNiuPictureBaseUrl();
        logger.info("------初始化ftp上传配置文件成功-----");
    }

    @Override
    public String upload(MultipartFile file,String suffix) {
        String path = DateUtil.dateTimeToStr(DateUtil.getNowDate(), DateUtil.YYYYMMDD)  + "/";
        String url = service.of(file).setPath(path).setPlatform(platform).setSaveFilename(file.getOriginalFilename()).upload().getUrl();
        return url;
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
