package com.helloscala.common.aspect;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.json.JSONUtil;
import com.helloscala.common.annotation.OperationLogger;
import com.helloscala.common.Constants;
import com.helloscala.common.entity.AdminLog;
import com.helloscala.common.entity.ExceptionLog;
import com.helloscala.common.mapper.AdminLogMapper;
import com.helloscala.common.mapper.ExceptionLogMapper;
import com.helloscala.common.utils.AspectUtil;
import com.helloscala.common.utils.DateUtil;
import com.helloscala.common.utils.IpUtil;
import com.helloscala.common.vo.user.SystemUserVO;
import com.helloscala.common.web.exception.ForbiddenException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

import static com.helloscala.common.Constants.CURRENT_USER;
import static com.helloscala.common.ResultCode.NO_PERMISSION;

@Aspect
@Component
@RequiredArgsConstructor
public class OperationLoggerAspect {
    private static final Logger logger = LoggerFactory.getLogger(OperationLoggerAspect.class);

    private final AdminLogMapper adminLogMapper;

    private final ExceptionLogMapper exceptionLogMapper;

    @Pointcut(value = "@annotation(operationLogger)")
    public void pointcut(OperationLogger operationLogger) {

    }

    @Around(value = "pointcut(operationLogger)", argNames = "joinPoint,operationLogger")
    public Object doAround(ProceedingJoinPoint joinPoint, OperationLogger operationLogger) throws Throwable {
        HttpServletRequest request = IpUtil.getRequest();
        StpUtil.checkLogin();
        if (!StpUtil.hasRole(Constants.ADMIN_CODE)) {
            throw new ForbiddenException(NO_PERMISSION.desc);
        }
        Date startTime = DateUtil.getNowDate();
        Object result = joinPoint.proceed();
        Date endTime = DateUtil.getNowDate();
        Long spendMills = endTime.getTime() - startTime.getTime();
        handle(spendMills, joinPoint, request);
        return result;
    }


    @Async
    @AfterThrowing(value = "pointcut(operationLogger)", throwing = "e", argNames = "joinPoint,operationLogger,e")
    public void doAfterThrowing(JoinPoint joinPoint, OperationLogger operationLogger, Throwable e) throws Exception {
        String ip = IpUtil.getIp();
        String operationName = AspectUtil.INSTANCE.parseParams(joinPoint.getArgs(), operationLogger.value());
        String paramsJson = getParamsJson((ProceedingJoinPoint) joinPoint);
        SystemUserVO user = (SystemUserVO) StpUtil.getSession().get(CURRENT_USER);

        ExceptionLog exception = ExceptionLog.builder().ip(ip).ipSource(IpUtil.getIp2region(ip))
            .params(paramsJson).username(user.getUsername()).method(joinPoint.getSignature().getName())
            .exceptionJson(JSONUtil.toJsonStr(e)).exceptionMessage(e.getMessage()).operation(operationName)
            .createTime(DateUtil.getNowDate()).build();
        exceptionLogMapper.insert(exception);
    }


    @Async
    public void handle(Long spendMills, ProceedingJoinPoint point, HttpServletRequest request) throws Exception {
        Method currentMethod = AspectUtil.INSTANCE.getMethod(point);
        OperationLogger annotation = currentMethod.getAnnotation(OperationLogger.class);
        if (Objects.isNull(annotation)) {
            return;
        }
        String operationName = AspectUtil.INSTANCE.parseParams(point.getArgs(), annotation.value());
        boolean save = annotation.save();
        if (!save) {
            logger.info("Ignore save log, on {}", point.getKind());
            return;
        }
        try {
            String paramsJson = getParamsJson(point);
            SystemUserVO user = (SystemUserVO) StpUtil.getSession().get(CURRENT_USER);
            String userName = "";
            if (!Objects.isNull(user)) {
                userName = user.getNickname();
            }
            String type = request.getMethod();
            String ip = IpUtil.getIp();
            String url = request.getRequestURI();
            AdminLog adminLog = new AdminLog(ip, IpUtil.getIp2region(ip), type, url, userName,
                    paramsJson, point.getTarget().getClass().getName(),
                    point.getSignature().getName(), operationName, spendMills);
            adminLogMapper.insert(adminLog);
        } catch (Exception e) {
            logger.error("Failed to add user log on {}", point.getKind(), e);
        }
    }

    private String getParamsJson(ProceedingJoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        String[] parameterNames = methodSignature.getParameterNames();

        HashMap<String, Object> paramMap = new HashMap<>();
        if (parameterNames != null) {
            for (int i = 0; i < parameterNames.length; i++) {
                paramMap.put(parameterNames[i], args[i]);
            }
        }

        boolean isContains = paramMap.containsKey("request");
        if (isContains) paramMap.remove("request");
        return JSONUtil.toJsonStr(paramMap);
    }
}
