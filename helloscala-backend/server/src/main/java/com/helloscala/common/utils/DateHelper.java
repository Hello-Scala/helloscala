package com.helloscala.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Steve Zou
 */
public final class DateHelper {
    private final static SimpleDateFormat MONTH_FORMAT = new SimpleDateFormat("yyyy-MM");

    public static String toYearAndMonth(Date date) {
        return MONTH_FORMAT.format(date);
    }

}
