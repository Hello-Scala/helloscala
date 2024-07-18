package com.helloscala.common.file;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.ftp.Ftp;
import com.helloscala.common.Constants;
import com.helloscala.common.config.FtpConfig;
import com.helloscala.common.utils.DateUtil;
import com.helloscala.common.web.exception.BadRequestException;
import com.helloscala.common.web.exception.FailedDependencyException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

/**
 * @author Steve Zou
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FtpService {
    private final FtpConfig ftpConfig;
    @Value(value = "${spring.servlet.multipart.max-file-size:5MB}")
    private String maxFileSizeStr;
    private Ftp ftpClient;

    @PostConstruct
    public void init() {
        if (Objects.isNull(ftpClient)) {
            ftpClient = new Ftp(ftpConfig.getHost(), ftpConfig.getPort(), ftpConfig.getUsername(), ftpConfig.getPassword());
        }
    }

    public String upload(MultipartFile file) {
        if (file.getSize() > getMaxFileSize().toBytes()) {
            throw new BadRequestException("文件大小不能大于10M");
        }
        String suffix = Objects.requireNonNull(file.getOriginalFilename()).substring(file.getOriginalFilename().lastIndexOf(".") + 1);
        if (!Constants.FIELD_SUFFIX.toUpperCase().contains(suffix.toUpperCase())) {
            throw new BadRequestException("请选择jpg,jpeg,gif,png,mp4格式的图片");
        }
        String storagePath = ftpConfig.getStoragePath();
        String path = DateUtil.dateTimeToStr(DateUtil.getNowDate(), DateUtil.YYYYMMDD) + "/";
        String filename = file.getOriginalFilename();
        String absolutePath = storagePath + path;

        boolean dirExist = ftpClient.isDir(absolutePath);
        if (!dirExist) {
            ftpClient.mkDirs(absolutePath);
        }
        try {
            InputStream inputStream = file.getInputStream();
            boolean upload = ftpClient.upload(absolutePath, filename, inputStream);
            if (!upload) {
                throw new FailedDependencyException("Failed to upload file to ftp server, fileName={}", filename);
            }
            return StrUtil.removeSuffix(ftpConfig.getDomain(), "/") + StrUtil.removeSuffix(absolutePath, "/") + "/" + filename;
        } catch (Exception e) {
            throw new FailedDependencyException(e, "Failed to upload file to ftp server, fileName={}", filename);
        }
    }

    public void download(String key, OutputStream outputStream) {
        String[] split = key.split("/");
        String fileName = split[split.length - 1];
        String path = StrUtil.removeSuffix(key, fileName);
        ftpClient.download(path, key, outputStream);
    }

    private DataSize getMaxFileSize() {
        if (maxFileSizeStr != null) {
            return DataSize.parse(maxFileSizeStr);
        } else {
            return DataSize.ofMegabytes(5);
        }
    }
}
