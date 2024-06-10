package com.helloscala.vo.service;

import lombok.Getter;

/**
 * @author Steve Zou
 */
@Getter
public class DurationView {
    private static final int PER_DAY_SECONDS = 60 * 60 * 24;
    private static final int PER_HOUR_SECONDS = 60 * 60;
    private static final int PER_MINUTE_SECONDS = 60;

    private final Long day;

    private final Integer hour;

    private final Integer minute;

    private final Integer second;

    public DurationView(Long seconds) {
        day = seconds / PER_DAY_SECONDS;
        hour = (int) (seconds % PER_DAY_SECONDS / PER_HOUR_SECONDS);
        minute = (int) (seconds % PER_HOUR_SECONDS / PER_MINUTE_SECONDS);
        second = (int) (seconds % PER_MINUTE_SECONDS);
    }
}
