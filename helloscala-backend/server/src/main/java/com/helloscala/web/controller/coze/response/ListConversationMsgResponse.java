package com.helloscala.web.controller.coze.response;

import lombok.Data;

import java.util.List;

/**
 * @author Steve Zou
 */
@Data
public class ListConversationMsgResponse {
    private Integer currentPage;

    private Integer totalPage;

    private Integer total;

    private List<AssistantMsgView> msgs;
}
