package com.helloscala.common.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Steve Zou
 */
public final class ListHelper {
    public static <T> List<T> ofNullable(List<T> tList) {
        return Objects.isNull(tList) ? new ArrayList<>() : tList;
    }
}
