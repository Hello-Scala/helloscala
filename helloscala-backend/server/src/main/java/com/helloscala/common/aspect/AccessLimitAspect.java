package com.helloscala.common.aspect;

import com.helloscala.common.annotation.AccessLimit;
import com.helloscala.common.utils.IpUtil;
import com.helloscala.common.web.exception.TooManyRequestsException;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Aspect
@Component
@RequiredArgsConstructor
public class AccessLimitAspect {

    private static final Logger logger = LoggerFactory.getLogger(AccessLimitAspect.class);
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Before("@annotation(accessLimit)")
    public void doBefore(JoinPoint joinPoint, AccessLimit accessLimit) throws TooManyRequestsException {
        int time = accessLimit.time();
        HttpServletRequest request = IpUtil.getRequest();
        if (Objects.isNull(request)) {
            logger.warn("Failed to get request on {}", joinPoint.getKind());
            return;
        }
        String key = IpUtil.getIp() + request.getRequestURI();
        Integer maxTimes = null;
        Object value = redisTemplate.opsForValue().get(key);
        if (value != null) {
            maxTimes = (Integer) value;
        }
        if (maxTimes == null) {
            redisTemplate.opsForValue().set(key, 1, time, TimeUnit.SECONDS);
        } else if (maxTimes < accessLimit.count()) {
            redisTemplate.opsForValue().set(key, maxTimes + 1, time, TimeUnit.SECONDS);
        } else {
            logger.info("Request too frequently, {}", key);
            throw new TooManyRequestsException("请求过于频繁，请求稍后重试！");
        }
    }
}
