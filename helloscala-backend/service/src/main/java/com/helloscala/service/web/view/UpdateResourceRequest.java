package com.helloscala.service.web.view;

import lombok.Data;

import java.util.Date;

@Data
public class UpdateResourceRequest {
    private Long id;

    private String url;

    private String type;

    private String platform;

    private String userId;

    private String requestBy;
}