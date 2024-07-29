package com.helloscala.web.controller.coze.response;

import lombok.Data;

import java.util.Date;

/**
 * @author Steve Zou
 */
@Data
public class AssistantSummaryView {
    private String id;

    private String name;

    private String description;

    private String iconUrl;

    private Date publishedTime;
}
