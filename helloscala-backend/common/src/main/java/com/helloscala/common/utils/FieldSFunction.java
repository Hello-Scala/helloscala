package com.helloscala.common.utils;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.helloscala.common.web.exception.GenericException;

import java.lang.reflect.Field;

/**
 * @author Steve Zou
 */
public class FieldSFunction<T> implements SFunction<T, Object> {
    private final Field field;
    private final Class<T> tClass;

    public FieldSFunction(Class<T> tClass, Field field) {
        this.field = field;
        this.tClass = tClass;
    }

    @Override
    public Object apply(T t) {
        try {
            return field.get(t);
        } catch (IllegalAccessException e) {
            throw new GenericException("Failed to get field value from entity={}, fieldName={}",
                    tClass.getSimpleName(), field.getName(), e);
        }
    }
}
