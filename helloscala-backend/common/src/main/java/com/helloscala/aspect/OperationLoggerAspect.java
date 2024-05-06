package com.helloscala.aspect;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.json.JSONUtil;
import com.helloscala.annotation.OperationLogger;
import com.helloscala.common.Constants;
import com.helloscala.entity.AdminLog;
import com.helloscala.entity.ExceptionLog;
import com.helloscala.exception.BusinessException;
import com.helloscala.mapper.AdminLogMapper;
import com.helloscala.mapper.ExceptionLogMapper;
import com.helloscala.utils.AspectUtil;
import com.helloscala.utils.DateUtil;
import com.helloscala.utils.IpUtil;
import com.helloscala.vo.user.SystemUserVO;
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

import static com.helloscala.common.Constants.CURRENT_USER;
import static com.helloscala.common.ResultCode.NO_PERMISSION;

@Aspect
@Component
@RequiredArgsConstructor
public class OperationLoggerAspect {

    private static final Logger logger = LoggerFactory.getLogger(OperationLoggerAspect.class);

    private final AdminLogMapper adminLogMapper;

    private final ExceptionLogMapper exceptionLogMapper;

    /**
     * 开始时间
     */
    Date startTime;

    @Pointcut(value = "@annotation(operationLogger)")
    public void pointcut(OperationLogger operationLogger) {

    }

    @Around(value = "pointcut(operationLogger)", argNames = "joinPoint,operationLogger")
    public Object doAround(ProceedingJoinPoint joinPoint, OperationLogger operationLogger) throws Throwable {
        HttpServletRequest request = IpUtil.getRequest();
        StpUtil.checkLogin();
        //因给了演示账号所有权限以供用户观看，所以执行业务前需判断是否是管理员操作
        if (!StpUtil.hasRole(Constants.ADMIN_CODE)) {
            throw new BusinessException(NO_PERMISSION);
        }
        startTime = DateUtil.getNowDate();

        //先执行业务
        Object result = joinPoint.proceed();
        try {
            // 日志收集
            handle(joinPoint, request);

        } catch (Exception e) {
            logger.error("日志记录出错!", e);
        }

        return result;
    }


    @Async
    @AfterThrowing(value = "pointcut(operationLogger)", throwing = "e", argNames = "joinPoint,operationLogger,e")
    public void doAfterThrowing(JoinPoint joinPoint, OperationLogger operationLogger, Throwable e) throws Exception {
        String ip = IpUtil.getIp();
        String operationName = AspectUtil.INSTANCE.parseParams(joinPoint.getArgs(), operationLogger.value());
        // 获取参数名称字符串
        String paramsJson = getParamsJson((ProceedingJoinPoint) joinPoint);
        SystemUserVO user = (SystemUserVO) StpUtil.getSession().get(CURRENT_USER);

        ExceptionLog exception = ExceptionLog.builder().ip(ip).ipSource(IpUtil.getIp2region(ip))
            .params(paramsJson).username(user.getUsername()).method(joinPoint.getSignature().getName())
            .exceptionJson(JSONUtil.toJsonStr(e)).exceptionMessage(e.getMessage()).operation(operationName)
            .createTime(DateUtil.getNowDate()).build();
        exceptionLogMapper.insert(exception);
    }

    /**
     * 管理员日志收集
     *
     * @param point
     * @throws Exception
     */
    private void handle(ProceedingJoinPoint point, HttpServletRequest request) throws Exception {

        Method currentMethod = AspectUtil.INSTANCE.getMethod(point);

        //获取操作名称
        OperationLogger annotation = currentMethod.getAnnotation(OperationLogger.class);

        boolean save = annotation.save();

        String operationName = AspectUtil.INSTANCE.parseParams(point.getArgs(), annotation.value());
        if (!save) {
            return;
        }
        // 获取参数名称字符串
        String paramsJson = getParamsJson(point);

        // 当前操作用户
        SystemUserVO user = (SystemUserVO) StpUtil.getSession().get(CURRENT_USER);
        String type = request.getMethod();
        String ip = IpUtil.getIp();
        String url = request.getRequestURI();

        // 存储日志
        Date endTime = new Date();
        Long spendTime = endTime.getTime() - startTime.getTime();
        AdminLog adminLog = new AdminLog(ip, IpUtil.getIp2region(ip), type, url, user.getNickname(),
            paramsJson, point.getTarget().getClass().getName(),
            point.getSignature().getName(), operationName, spendTime);
        adminLogMapper.insert(adminLog);
    }

    private String getParamsJson(ProceedingJoinPoint joinPoint) throws ClassNotFoundException, NoSuchMethodException {
        // 参数值
        Object[] args = joinPoint.getArgs();
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        String[] parameterNames = methodSignature.getParameterNames();

        // 通过map封装参数和参数值
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
