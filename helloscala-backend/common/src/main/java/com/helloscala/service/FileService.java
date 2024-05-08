package com.helloscala.service;

import com.helloscala.common.ResponseResult;
import jakarta.servlet.ServletResponse;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    /**
     * 上传
     * @param file 文件
     * @return
     */
    ResponseResult upload(MultipartFile file);

    void download(String url, ServletResponse response);

    /**
     * 批量删除文件
     * @param key 文件名
     * @return
     */
    ResponseResult delBatchFile(String ...key);
}