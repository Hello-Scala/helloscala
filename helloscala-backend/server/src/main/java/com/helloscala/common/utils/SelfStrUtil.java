package com.helloscala.common.utils;

import cn.hutool.core.util.StrUtil;
import org.jetbrains.annotations.Nullable;

public final class SelfStrUtil {
    @Nullable
    public static String removeHeadAndTail(String str, String head, String tail) {
        String removedPrefix = StrUtil.removePrefix(str, head);
        String value = StrUtil.removeSuffix(removedPrefix, tail);
        return value;
    }
}
