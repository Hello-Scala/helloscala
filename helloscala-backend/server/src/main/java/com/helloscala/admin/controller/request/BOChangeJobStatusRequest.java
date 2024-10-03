package com.helloscala.admin.controller.request;

import lombok.Data;

@Data
public class BOChangeJobStatusRequest {
    private String jobId;

    private String status;
}
