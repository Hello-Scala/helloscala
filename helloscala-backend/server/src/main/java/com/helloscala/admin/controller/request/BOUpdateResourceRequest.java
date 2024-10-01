package com.helloscala.admin.controller.request;

import lombok.Data;

import java.util.Date;

@Data
public class BOUpdateResourceRequest {
    private Long id;

    private String url;

    private String type;

    private String platform;

    private String userId;
}