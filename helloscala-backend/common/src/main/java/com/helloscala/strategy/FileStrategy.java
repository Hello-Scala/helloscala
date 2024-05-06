package com.helloscala.strategy;

import jakarta.servlet.ServletResponse;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传策略
 */
// todo refactor
public interface FileStrategy {
    /**
     * 上传文件
     * @param file
     * @param suffix
     * @return
     */
    String upload(MultipartFile file, String suffix);

    void download(String key, ServletResponse response);


    /**
     * 删除文件
     * @param fileName
     * @return
     */
    Boolean delete(String ...fileName);
}
