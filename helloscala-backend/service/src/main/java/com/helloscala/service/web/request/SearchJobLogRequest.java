package com.helloscala.service.web.request;

import lombok.Data;

@Data
public class SearchJobLogRequest {
    private String jobName;

    private String jobGroup;

    private String status;

    private String startTime;

    private String endTime;

    private Long id;
}
