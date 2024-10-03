package com.helloscala.service.web.request;

import lombok.Data;

@Data
public class SearchCommentRequest {
    private String nickNameLike;
    private String userId;
    private String articleId;
}
