package com.helloscala.vo.service;

import cn.hutool.core.util.StrUtil;
import lombok.Data;

@Data
public class SysCacheVO {
    /** 缓存名称 */
    private String cacheName = "";

    /** 缓存键名 */
    private String cacheKey = "";

    /** 缓存内容 */
    private String cacheValue = "";

    /** 备注 */
    private String remark = "";

    /** 是否访客显示 */
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
