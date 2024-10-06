package com.helloscala.common.utils;

import com.baomidou.mybatisplus.core.toolkit.LambdaUtils;
import com.baomidou.mybatisplus.core.toolkit.support.LambdaMeta;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.helloscala.common.web.exception.GenericException;
import org.apache.ibatis.reflection.property.PropertyNamer;

import java.lang.reflect.Field;

/**
 * @author Steve Zou
 */
public class SqlHelper {
    public static final String LIMIT_1 = " LIMIT 1 ";

    public static <T> SFunction<T, ?> toFieldFunc(Class<T> tableClass, String fieldName) {
        try {
            Field field = tableClass.getDeclaredField(fieldName);
            return new FieldSFunction<>(tableClass, field);
        } catch (NoSuchFieldException e) {
            throw new GenericException("Field not fund in tableClass={}", tableClass.getSimpleName());
        }
    }

    public static <T> String getFieldName(SFunction<T, ?> func) {
        LambdaMeta meta = LambdaUtils.extract(func);
        return PropertyNamer.methodToProperty(meta.getImplMethodName());
    }
}
