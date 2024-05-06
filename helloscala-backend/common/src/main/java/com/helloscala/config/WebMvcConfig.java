package com.helloscala.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.stp.StpUtil;
import com.helloscala.config.intercept.PageableInterceptor;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer  {
    @Resource
    private PageableInterceptor pageableInterceptor;

    @Value("${file.upload-folder}")
    private String UPLOAD_FOLDER;


    /**
     * 注册sa-token的拦截器，打开注解式鉴权功能 (如果您不需要此功能，可以删除此类)
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //分页拦截器
        registry.addInterceptor(pageableInterceptor);


        // 注册Sa-Token的路由拦截器 https://blog.csdn.net/wu2374633583/article/details/131559324
        registry.addInterceptor(new SaInterceptor(handler -> StpUtil.checkLogin()))
            .addPathPatterns("/system/**").excludePathPatterns("/login","/logout","/verify", "/asserts");

    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

            registry.addResourceHandler("/webjars/**").addResourceLocations(
                    "classpath:/META-INF/resources/webjars/");
        registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");
//        registry.addResourceHandler("/img/**").addResourceLocations("classpath:/META-INF/resources/" + UPLOAD_FOLDER);
    }

    /**
     * 注册跨域信息
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*") // 允许所有跨域地址
                .allowedHeaders("*")
                .allowedMethods("*")
                .maxAge(3600);
    }

}
