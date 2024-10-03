package com.helloscala.admin.controller.request;

import lombok.Data;

@Data
public class BOSearchJobLogRequest {
    private String jobName;

    private String jobGroup;

    private String status;

    private String startTime;

    private String endTime;

    private Long jobId;
}
