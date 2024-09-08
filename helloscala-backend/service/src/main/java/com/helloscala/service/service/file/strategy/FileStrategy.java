package com.helloscala.service.service.file.strategy;

import jakarta.servlet.ServletResponse;
import org.springframework.web.multipart.MultipartFile;

// todo refactor
public interface FileStrategy {
    String upload(MultipartFile file, String suffix);

    void download(String key, ServletResponse response);

    Boolean delete(String ...fileName);
}
