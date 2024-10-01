package com.helloscala.admin.controller.request;

import lombok.Data;

@Data
public class BOCreateResourceRequest {
    private String url;

    private String type;

    private String platform;

    private String userId;
}