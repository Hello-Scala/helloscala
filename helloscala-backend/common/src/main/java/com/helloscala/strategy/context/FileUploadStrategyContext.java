package com.helloscala.strategy.context;

import com.helloscala.strategy.FileStrategy;
import jakarta.servlet.ServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;


/**
 * @apiNote 文件上传策略上下文
 */
@Service
@RequiredArgsConstructor
public class FileUploadStrategyContext {

    private final Map<String, FileStrategy> fileUploadStrategyMap;

    /**
     * 执行文件上传策略
     *
     * @param file 文件对象
     * @return {@link String} 文件名
     */
    public String executeUpload(String fileUploadMode, MultipartFile file, String suffix) {
         return fileUploadStrategyMap.get(fileUploadMode).upload(file,suffix);
    }

    public void executeDownload(String fileUploadMode, String key, ServletResponse response) {
        fileUploadStrategyMap.get(fileUploadMode).download(key, response);
    }
    /**
     * 删除文件策略
     * @param fileUploadMode
     * @param key
     * @return
     */
    public Boolean executeDelete(String fileUploadMode, String ...key) {
         return fileUploadStrategyMap.get(fileUploadMode).delete(key);
    }
}
