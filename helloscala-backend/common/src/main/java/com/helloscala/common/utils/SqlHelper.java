package com.helloscala.common.utils;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.helloscala.common.web.exception.GenericException;

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
}
