package com.helloscala.common.service;

import com.helloscala.common.ResponseResult;
import jakarta.servlet.ServletResponse;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    ResponseResult upload(MultipartFile file);

    void download(String url, ServletResponse response);

    ResponseResult delBatchFile(String... key);
}
