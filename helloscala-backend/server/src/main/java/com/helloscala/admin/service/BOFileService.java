package com.helloscala.admin.service;

import com.helloscala.service.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class BOFileService {
    private final FileService fileService;

    public String upload(String userId, MultipartFile multipartFile) {
        String key = fileService.upload(multipartFile);
        log.info("file upload success, key={}, userId={}", key, userId);
        return key;
    }

    public void delete(String userId, String key) {
        fileService.delBatchFile(key);
        log.info("file delete success, key={}, userId={}", key, userId);
    }
}
