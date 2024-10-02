package com.helloscala.admin.controller.view;

import lombok.Data;

@Data
public class BOUserIPCountView {
    private String ip;

    private Integer access;

    private String createTime;
}
