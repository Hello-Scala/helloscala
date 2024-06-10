package com.helloscala.common.annotation;

import java.lang.annotation.*;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AccessLimit {

    // Access times limit during limited duration
    public int count() default 10;

    // Access limit seconds
    public int time() default 10;


    // Allow access over limitation count
    public boolean pass() default false;

}
