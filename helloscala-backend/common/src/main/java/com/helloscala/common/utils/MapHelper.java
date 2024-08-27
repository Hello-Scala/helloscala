package com.helloscala.common.utils;

import java.util.*;

/**
 * @author Steve Zou
 */
public final class MapHelper {
    public static <K, V> Map<K, V> ofNullable(Map<K, V> map) {
        return Objects.isNull(map) ? Map.of() : map;
    }

    public static <K, V> Map<K, V> concat(Map<K, V> map1, Map<K, V> map2) {
        Map<K, V> res = new HashMap<>();
        res.putAll(map1);
        res.putAll(map2);
        return res;
    }
}
