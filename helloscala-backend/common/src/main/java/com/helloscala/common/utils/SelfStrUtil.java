package com.helloscala.common.utils;

import cn.hutool.core.util.StrUtil;
import jakarta.annotation.Nullable;

public final class SelfStrUtil {
    @Nullable
    public static String removeHeadAndTail(String str, String head, String tail) {
        String removedPrefix = StrUtil.removePrefix(str, head);
        return StrUtil.removeSuffix(removedPrefix, tail);
    }
}
