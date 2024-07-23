package com.helloscala.web.controller.coze.request;

import lombok.Data;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Steve Zou
 */
@Data
public class ListConversationRequest {
    private String id;

    private Integer pageNo;

    private Integer pageSize;
}
