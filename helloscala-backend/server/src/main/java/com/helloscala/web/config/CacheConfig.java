package com.helloscala.web.config;

import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.time.Duration;

/**
 * @author Steve Zou
 */
@Configuration
@RequiredArgsConstructor
public class CacheConfig {
    private static final long TTL_MINUTES = 15;

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        GenericFastJsonRedisSerializer genericFastJsonRedisSerializer = new GenericFastJsonRedisSerializer();
        RedisSerializationContext.SerializationPair<String> stringSerializationPair = RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.string());
        RedisCacheConfiguration cacheConfiguration = RedisCacheConfiguration.defaultCacheConfig().serializeKeysWith(stringSerializationPair)
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(genericFastJsonRedisSerializer))
                .entryTtl(Duration.ofMinutes(TTL_MINUTES));
        return RedisCacheManager.builder(connectionFactory).cacheDefaults(cacheConfiguration).build();
    }
}
