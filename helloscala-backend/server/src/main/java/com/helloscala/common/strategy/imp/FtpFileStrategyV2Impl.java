package com.helloscala.common.strategy.imp;

import com.helloscala.common.file.FtpService;
import com.helloscala.common.strategy.FileStrategy;
import com.helloscala.common.web.exception.BadRequestException;
import com.helloscala.common.web.exception.FailedDependencyException;
import jakarta.servlet.ServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service("ftpFileStrategyV2Impl")
@RequiredArgsConstructor
public class FtpFileStrategyV2Impl implements FileStrategy {
    private final FtpService ftpService;

    @Override
    public String upload(MultipartFile file, String suffix) {
        return ftpService.upload(file);
    }

    @Override
    public Boolean delete(String... keys) {
        throw new BadRequestException("Unsupported operation!");
    }

    @Override
    public void download(String key, ServletResponse response) {
        try {
            ftpService.download(key, response.getOutputStream());
        } catch (Exception e) {
            throw new FailedDependencyException(e, "Failed to download file, key={}!", key);
        }
    }
}
