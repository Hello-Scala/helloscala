package com.helloscala.web.controller.coze.response;

import lombok.Data;

import java.util.List;

/**
 * @author Steve Zou
 */
@Data
public class ListAssistantResponse {
    private List<AssistantSummaryView> assistantSummaries;
}
