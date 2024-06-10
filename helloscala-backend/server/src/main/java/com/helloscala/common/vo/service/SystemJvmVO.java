package com.helloscala.common.vo.service;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;
import java.lang.management.ManagementFactory;
import java.time.ZonedDateTime;
import java.util.Date;

@Data
public class SystemJvmVO implements Serializable {
    private double total;

    private double max;

    private double free;

    private String version;

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

    public double getUsage() {
        return NumberUtil.mul(NumberUtil.div(total - free, total, 4), 100);
    }

    public String getName() {
        return ManagementFactory.getRuntimeMXBean().getVmName();
    }

    public ZonedDateTime getStartTime() {
        long time = ManagementFactory.getRuntimeMXBean().getStartTime();
        Date date = new Date(time);
        return ZonedDateTime.from(date.toInstant());
    }

    public DurationView getRunTime() {
        long time = ManagementFactory.getRuntimeMXBean().getStartTime();
        Date date = new Date(time);
        long runMS = DateUtil.between(date, new Date(), DateUnit.SECOND);
        return new DurationView(runMS);
    }
}
