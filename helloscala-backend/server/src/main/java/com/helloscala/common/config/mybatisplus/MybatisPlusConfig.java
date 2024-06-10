package com.helloscala.common.config.mybatisplus;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MybatisPlusConfig {

    /**
     * 设置自动注入规则
     *
     * @date: 2021/7/19 14:52
     */
    @Bean
    public MetaObjectHandler metaObjectHandler() {
        return new MyMetaObjectHandler();
    }
}
