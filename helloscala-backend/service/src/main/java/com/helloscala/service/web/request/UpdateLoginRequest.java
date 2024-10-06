package com.helloscala.service.web.request;

import lombok.Data;

@Data
public class UpdateLoginRequest {
    private String id;

    private String ip;

    private String city;

    private String os;

    private String browser;

    private String requestBy;
}
