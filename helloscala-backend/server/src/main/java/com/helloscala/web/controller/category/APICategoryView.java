package com.helloscala.web.controller.category;

import lombok.Data;

@Data
public class APICategoryView {
    private String id;

    private String name;

    private String icon;

    private Integer articleNum;
}
