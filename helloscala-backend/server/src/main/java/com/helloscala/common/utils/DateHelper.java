package com.helloscala.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

/**
 * @author Steve Zou
 */
public final class DateHelper {
    private final static SimpleDateFormat MONTH_FORMAT = new SimpleDateFormat("yyyy-MM");

    public static String toYearAndMonth(Date date) {
        return MONTH_FORMAT.format(date);
    }

    public static Optional<Date> toDate(Long seconds) {
        return Objects.isNull(seconds) ?  Optional.empty() : Optional.of(new Date(seconds * 1000L));
    }
}
