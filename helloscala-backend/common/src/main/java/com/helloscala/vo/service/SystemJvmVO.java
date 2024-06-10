package com.helloscala.vo.service;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import lombok.Data;

import java.io.Serializable;
import java.lang.management.ManagementFactory;
import java.time.ZonedDateTime;
import java.util.Date;

@Data
public class SystemJvmVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 当前JVM占用的内存总数(M)
     */
    private double total;

    /**
     * JVM最大可用内存总数(M)
     */
    private double max;

    /**
     * JVM空闲内存(M)
     */
    private double free;

    /**
     * JDK版本
     */
    private String version;

    /**
     * JDK路径
     */
    private String home;

    public double getTotal() {
        return NumberUtil.div(total, (1024 * 1024), 2);
    }

    public double getMax() {
        return NumberUtil.div(max, (1024 * 1024), 2);
    }

    public double getFree() {
        return NumberUtil.div(free, (1024 * 1024), 2);
    }

    public double getUsed() {
        return NumberUtil.div(total - free, (1024 * 1024), 2);
    }

    public String getVersion() {
        return version;
    }

    public String getHome() {
        return home;
    }

    public double getUsage() {
        return NumberUtil.mul(NumberUtil.div(total - free, total, 4), 100);
    }
    /**
     * 获取JDK名称
     */
    public String getName() {
        return ManagementFactory.getRuntimeMXBean().getVmName();
    }

    /**
     * JDK start time
     */
    public ZonedDateTime getStartTime() {
        long time = ManagementFactory.getRuntimeMXBean().getStartTime();
        Date date = new Date(time);
        return ZonedDateTime.from(date.toInstant());
    }

    /**
     * JDK running duration
     */
    public DurationView getRunTime() {
        long time = ManagementFactory.getRuntimeMXBean().getStartTime();
        Date date = new Date(time);
        long runMS = DateUtil.between(date, new Date(), DateUnit.SECOND);
        return new DurationView(runMS);
    }
}
