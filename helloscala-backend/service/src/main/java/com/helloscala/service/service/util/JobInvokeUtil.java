package com.helloscala.service.service.util;

import cn.hutool.core.util.StrUtil;
import com.helloscala.service.entity.Job;
import com.helloscala.common.utils.SelfStrUtil;
import com.helloscala.common.utils.SpringUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

public class JobInvokeUtil {
    public static void invokeMethod(Job job) throws Exception {
        String invokeTarget = job.getInvokeTarget();
        String beanName = getBeanName(invokeTarget);
        String methodName = getMethodName(invokeTarget);
        List<Object[]> methodParams = getMethodParams(invokeTarget);

        if (!isValidClassName(beanName)) {
            Object bean = SpringUtil.getBean(beanName);
            invokeMethod(bean, methodName, methodParams);
        } else {
            Object bean = Class.forName(beanName).getDeclaredConstructor().newInstance();
            invokeMethod(bean, methodName, methodParams);
        }
    }

    private static void invokeMethod(Object bean, String methodName, List<Object[]> methodParams)
            throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException {
        if (methodParams != null && !methodParams.isEmpty()) {
            Method method = bean.getClass().getDeclaredMethod(methodName, getMethodParamsType(methodParams));
            method.invoke(bean, getMethodParamsValue(methodParams));
        } else {
            Method method = bean.getClass().getDeclaredMethod(methodName);
            method.invoke(bean);
        }
    }

    public static boolean isValidClassName(String invokeTarget) {
        return StrUtil.count(invokeTarget, ".") > 1;
    }

    public static String getBeanName(String invokeTarget) {
        String beanName = StrUtil.subBefore(invokeTarget, "(", false);
        return StrUtil.subBefore(beanName, ".", true);
    }

    public static String getMethodName(String invokeTarget) {
        String methodName = StrUtil.subBefore(invokeTarget, "(", true);
        return StrUtil.subAfter(methodName, ".", false);
    }

    public static List<Object[]> getMethodParams(String invokeTarget) {
        String methodStr = SelfStrUtil.removeHeadAndTail(invokeTarget, "(", ")");
        if (StrUtil.isEmpty(methodStr)) {
            return null;
        }
        String[] methodParams = methodStr.split(",(?=(?:[^\']*\"[^\']*\')*[^\']*$)");
        List<Object[]> classs = new LinkedList<>();
        for (String methodParam : methodParams) {
            String str = StrUtil.trimToEmpty(methodParam);
            if (StrUtil.contains(str, "'")) {
                classs.add(new Object[]{StrUtil.replace(str, "'", ""), String.class});
            } else if (StrUtil.equals(str, "true") || StrUtil.equalsIgnoreCase(str, "false")) {
                classs.add(new Object[]{Boolean.valueOf(str), Boolean.class});
            } else if (StrUtil.containsIgnoreCase(str, "L")) {
                classs.add(new Object[]{Long.valueOf(StrUtil.replaceIgnoreCase(str, "L", "")), Long.class});
            } else if (StrUtil.containsIgnoreCase(str, "D")) {
                classs.add(new Object[]{Double.valueOf(StrUtil.replaceIgnoreCase(str, "D", "")), Double.class});
            } else {
                classs.add(new Object[]{Integer.valueOf(str), Integer.class});
            }
        }
        return classs;
    }

    public static Class<?>[] getMethodParamsType(List<Object[]> methodParams) {
        Class<?>[] classs = new Class<?>[methodParams.size()];
        int index = 0;
        for (Object[] os : methodParams) {
            classs[index] = (Class<?>) os[1];
            index++;
        }
        return classs;
    }

    public static Object[] getMethodParamsValue(List<Object[]> methodParams) {
        Object[] classs = new Object[methodParams.size()];
        int index = 0;
        for (Object[] os : methodParams) {
            classs[index] = os[0];
            index++;
        }
        return classs;
    }
}
