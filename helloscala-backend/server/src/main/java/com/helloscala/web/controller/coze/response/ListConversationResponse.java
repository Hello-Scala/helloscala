package com.helloscala.web.controller.coze.response;

import lombok.Data;

import java.util.List;

/**
 * @author Steve Zou
 */
@Data
public class ListConversationResponse {
    private Integer currentPage;

    private Integer totalPage;

    private Integer total;

    private List<BOConversationView> conversations;
}
