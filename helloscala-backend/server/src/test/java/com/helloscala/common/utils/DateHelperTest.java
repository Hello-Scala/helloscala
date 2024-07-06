package com.helloscala.common.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Date;

/**
 * @author Steve Zou
 */
class DateHelperTest {

    @Test
    void toYearAndMonth() {
        Instant instant = Instant.parse("2024-07-03T13:29:59.642916746Z");
        Date date = Date.from(instant);
        String yearAndMonth = DateHelper.toYearAndMonth(date);
        Assertions.assertEquals("2024-07", yearAndMonth);
    }
}