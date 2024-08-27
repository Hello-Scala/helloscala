package com.helloscala.common.utils;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;
import java.util.function.Function;

/**
 * @author Steve Zou
 */
public final class PageHelper {

    public static <F, T> Page<T> convertTo(Page<F> from, Function<F, T> mapping) {
        List<T> records = from.getRecords().stream().map(mapping).toList();
        Page<T> page = Page.of(from.getCurrent(), from.getSize(), from.getTotal());
        page.setRecords(records);
        return page;
    }

    public static <T> Page<T> ofEmpty() {
        return Page.of(0, 0, 0);
    }

    public static <T> Page<T> of(Page<?> from) {
        return Page.of(from.getCurrent(), from.getSize());
    }
}
