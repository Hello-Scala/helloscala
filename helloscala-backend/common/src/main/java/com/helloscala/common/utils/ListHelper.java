package com.helloscala.common.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Steve Zou
 */
public final class ListHelper {
    public static <T> List<T> ofNullable(List<T> tList) {
        return Objects.isNull(tList) ? List.of() : tList;
    }

    public static <T> List<T> concat(List<T> ls1, List<T> ls2) {
        List<T> res = new ArrayList<>();
        res.addAll(ls1);
        res.addAll(ls2);
        return res;
    }
}
