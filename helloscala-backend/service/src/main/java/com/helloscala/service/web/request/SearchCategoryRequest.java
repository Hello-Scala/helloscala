package com.helloscala.service.web.request;

import lombok.Data;

/**
 * @author Steve Zou
 */
@Data
public class SearchCategoryRequest {
    private String ids;

    private String name;
}
