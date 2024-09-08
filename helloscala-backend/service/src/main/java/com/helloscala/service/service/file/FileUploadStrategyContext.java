package com.helloscala.service.service.file;

import com.helloscala.service.service.file.strategy.FileStrategy;
import jakarta.servlet.ServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;


@Service
@RequiredArgsConstructor
public class FileUploadStrategyContext {

    private final Map<String, FileStrategy> fileUploadStrategyMap;

    public String executeUpload(String fileUploadMode, MultipartFile file, String suffix) {
         return fileUploadStrategyMap.get(fileUploadMode).upload(file,suffix);
    }

    public void executeDownload(String fileUploadMode, String key, ServletResponse response) {
        fileUploadStrategyMap.get(fileUploadMode).download(key, response);
    }

    public Boolean executeDelete(String fileUploadMode, String ...key) {
         return fileUploadStrategyMap.get(fileUploadMode).delete(key);
    }
}
