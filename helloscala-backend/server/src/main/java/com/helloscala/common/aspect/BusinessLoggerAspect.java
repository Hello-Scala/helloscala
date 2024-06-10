package com.helloscala.common.aspect;

import com.helloscala.common.ResponseResult;
import com.helloscala.common.annotation.BusinessLogger;
import com.helloscala.common.entity.UserLog;
import com.helloscala.common.mapper.UserLogMapper;
import com.helloscala.common.utils.DateUtil;
import com.helloscala.common.utils.IpUtil;
import eu.bitwalker.useragentutils.UserAgent;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Objects;

@Aspect
@Component
@RequiredArgsConstructor
public class BusinessLoggerAspect {

    private static final Logger logger = LoggerFactory.getLogger(BusinessLoggerAspect.class);
    public static final String USER_AGENT = "user-agent";

    private final UserLogMapper sysLogMapper;

    @Pointcut("@annotation(businessLogger)")
    public void pointcut(BusinessLogger businessLogger) {
    }

    @Around(value = "pointcut(businessLogger)")
    public Object doAround(ProceedingJoinPoint joinPoint, BusinessLogger businessLogger) throws Throwable {
        Object result = joinPoint.proceed();
        handle(joinPoint,(ResponseResult) result);
        return result;
    }

    @Async
    public void handle(ProceedingJoinPoint  joinPoint, ResponseResult result) throws Throwable {
        HttpServletRequest request = IpUtil.getRequest();
        if (Objects.isNull(request)) {
            logger.error("Failed to get request, on {}", joinPoint.getKind());
            return;
        }
        try {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            BusinessLogger annotation = method.getAnnotation(BusinessLogger.class);
            if (Objects.isNull(annotation)) {
                return;
            }
            if (!annotation.save()) {
                logger.info("Ignore save log, on {}", joinPoint.getKind());
                return;
            }
            String ip = IpUtil.getIp();
            UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader(USER_AGENT));
            String clientType = userAgent.getOperatingSystem().getDeviceType().toString();
            String os = userAgent.getOperatingSystem().getName();
            String browser = userAgent.getBrowser().toString();

            UserLog userLog = UserLog.builder().model(annotation.value()).type(annotation.type())
                    .description(annotation.desc()).createTime(DateUtil.getNowDate())
                    .ip(ip).address(IpUtil.getIp2region(ip)).clientType(clientType).accessOs(os)
                    .browser(browser).result(result.getMessage()).build();
            sysLogMapper.insert(userLog);
        } catch (Exception e) {
            logger.error("Failed to add user log on {}", joinPoint.getKind(), e);
        }
    }
}
