package com.helloscala.config.filter;

import cn.hutool.core.util.StrUtil;
import com.helloscala.service.FileService;
import jakarta.annotation.Resource;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class FileDownLoadFilter implements Filter {
    @Resource
    private FileService fileService;
    @Value("${file.resource-prefix}")
    private String RESOURCE_PREFIX ;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (!(request instanceof HttpServletRequest servletRequest)) {
            chain.doFilter(request, response);
            return;
        }

        String requestURI = servletRequest.getRequestURI();
        if (!requestURI.startsWith(RESOURCE_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }
        fileService.download(requestURI, response);
    }
}
