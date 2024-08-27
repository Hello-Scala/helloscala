package com.helloscala.service.service;

import com.helloscala.service.entity.Sign;

import java.util.List;

public interface ApiSignService {
    List<String> getSignRecords(String startTime, String endTime);

    void sign(String time);

    Sign validateTodayIsSign();
}
