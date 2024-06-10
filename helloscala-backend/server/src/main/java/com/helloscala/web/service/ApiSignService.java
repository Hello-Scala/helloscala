package com.helloscala.web.service;

import com.helloscala.common.ResponseResult;

public interface ApiSignService {
    ResponseResult getSignRecords(String startTime, String endTime);

    ResponseResult sign(String time);

    ResponseResult validateTodayIsSign();
}
