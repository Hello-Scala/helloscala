package com.helloscala.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @title: AccessLimit
 * @description: 限流注解
 */
@Documented
@Target(ElementType.METHOD)//注解放置的目标位置即方法级别
@Retention(RetentionPolicy.RUNTIME)//注解在哪个阶段执行
public @interface AccessLimit {

    /**
     * 在时间窗内的限流次数
     * @return
     */
    public int count() default 10;

    /**
     * 限流时间窗
     * @return
     */
    public int time() default 10;  // 限流时间

    public boolean pass() default false;  // 超过限流次数是否也放行

}
