package com.helloscala.common.service;

import jakarta.servlet.ServletResponse;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    String upload(MultipartFile file);

    void download(String url, ServletResponse response);

    void delBatchFile(String... key);
}
