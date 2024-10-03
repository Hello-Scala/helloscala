package com.helloscala.service.web.request;


import lombok.Data;

@Data
public class ChangeJobStatusRequest {
    private String id;

    private String status;

    private String requestBy;
}
