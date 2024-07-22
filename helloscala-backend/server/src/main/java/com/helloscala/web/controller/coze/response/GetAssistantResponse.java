package com.helloscala.web.controller.coze.response;

import lombok.Data;

import java.util.List;

/**
 * @author Steve Zou
 */
@Data
public class GetAssistantResponse {
    private String botId;

    private String name;

    private String description;

    private String iconUrl;

    private long createTime;

    private long updateTime;

    private String version;

    private String prompt;

    private AssistantOnboardInfo onboardingInfo;

    private int botMode;

    private AssistantModelView model;

    private List<AssistantPlugView> plugins;
}
