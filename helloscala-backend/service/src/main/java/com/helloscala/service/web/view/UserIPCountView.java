package com.helloscala.service.web.view;

import lombok.Data;

@Data
public class UserIPCountView {
    private String ip;

    private Integer count;

    private String createDate;
}
