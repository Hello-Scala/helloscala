package com.helloscala.common.utils;

import java.util.*;

/**
 * @author Steve Zou
 */
public final class SetHelper {
    public static <T> Set<T> ofNullable(Set<T> tSet) {
        return Objects.isNull(tSet) ? Set.of() : tSet;
    }

    public static <T> Set<T> concat(Set<T> ls1, Set<T> ls2) {
        Set<T> res = new HashSet<>();
        res.addAll(ls1);
        res.addAll(ls2);
        return res;
    }
}
