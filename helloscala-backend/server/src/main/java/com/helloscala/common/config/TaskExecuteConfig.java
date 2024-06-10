package com.helloscala.common.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableAsync
public class TaskExecuteConfig {

    @Value("${task.executor.core_pool_size}")
    private int corePoolSize;
    @Value("${task.executor.max_pool_size}")
    private int maxPoolSize;
    @Value("${task.executor.queue_capacity}")
    private int queueCapacity;
    @Value("${task.executor.keep_alive_seconds}")
    private int keepAliveSeconds;

    // todo replace with application.yaml
    @Bean(name = "threadPoolTaskExecutor")
    public ThreadPoolTaskExecutor SendTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setKeepAliveSeconds(keepAliveSeconds);
        executor.setThreadNamePrefix("pool-send-task-executor");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }
}
