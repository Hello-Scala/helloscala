package com.helloscala.web.service;

import com.helloscala.common.entity.Sign;

import java.util.List;

public interface ApiSignService {
    List<String> getSignRecords(String startTime, String endTime);

    void sign(String time);

    Sign validateTodayIsSign();
}
