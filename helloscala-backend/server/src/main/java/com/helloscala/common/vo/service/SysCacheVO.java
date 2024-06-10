package com.helloscala.common.vo.service;

import cn.hutool.core.util.StrUtil;
import lombok.Data;

@Data
public class SysCacheVO {
    private String cacheName = "";

    private String cacheKey = "";

    private String cacheValue = "";

    private String remark = "";

    private boolean show = true;

    public SysCacheVO(String cacheName, String remark,boolean show)
    {
        this.cacheName = cacheName;
        this.remark = remark;
        this.show = show;
    }

    public SysCacheVO(String cacheName, String cacheKey, String cacheValue)
    {
        this.cacheName = StrUtil.replace(cacheName, ":", "");
        this.cacheKey = StrUtil.replace(cacheKey, cacheName, "");
        this.cacheValue = cacheValue;
    }
}
