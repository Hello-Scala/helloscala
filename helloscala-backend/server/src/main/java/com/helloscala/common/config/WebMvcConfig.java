package com.helloscala.common.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.stp.StpUtil;
import com.helloscala.common.config.intercept.PageableInterceptor;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer {
    @Resource
    private PageableInterceptor pageableInterceptor;

    @Value("${file.upload-folder}")
    private String UPLOAD_FOLDER;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(pageableInterceptor);


        // ref https://blog.csdn.net/wu2374633583/article/details/131559324
        registry.addInterceptor(new SaInterceptor(handler -> StpUtil.checkLogin()))
                .addPathPatterns("/system/**").excludePathPatterns("/login", "/logout", "/verify", "/asserts");

    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler("/webjars/**").addResourceLocations(
                "classpath:/META-INF/resources/webjars/");
        registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");
//        registry.addResourceHandler("/img/**").addResourceLocations("classpath:/META-INF/resources/" + UPLOAD_FOLDER);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedHeaders("*")
                .allowedMethods("*")
                .maxAge(3600);
    }
}
