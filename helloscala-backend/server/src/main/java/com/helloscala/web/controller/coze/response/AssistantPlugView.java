package com.helloscala.web.controller.coze.response;

import lombok.Data;

import java.util.List;

/**
 * @author Steve Zou
 */
@Data
public class AssistantPlugView {
    private String id;

    private String name;

    private String description;

    private String iconUrl;

    private List<AssistantPlugAPIView> apiList;
}
