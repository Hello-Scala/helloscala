package com.helloscala.admin.controller.view;

import lombok.Data;

import java.util.Date;

@Data
public class BOResourceView {
    private String id;

    private String url;

    private String type;

    private String platform;

    private String userId;

    private Date createTime;
}