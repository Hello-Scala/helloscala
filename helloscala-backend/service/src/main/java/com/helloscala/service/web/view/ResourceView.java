package com.helloscala.service.web.view;

import lombok.Data;

import java.util.Date;

@Data
public class ResourceView {
    private String id;

    private String url;

    private String type;

    private String platform;

    private String userId;

    private Date createTime;
}