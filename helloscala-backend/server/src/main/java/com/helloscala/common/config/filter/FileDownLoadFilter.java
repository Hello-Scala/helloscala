package com.helloscala.common.config.filter;

import com.helloscala.common.service.FileService;
import jakarta.annotation.Resource;
import jakarta.servlet.*;
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
