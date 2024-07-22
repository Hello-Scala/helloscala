package com.helloscala.web.controller.coze.response;

import lombok.Data;

import java.util.Date;

/**
 * @author Steve Zou
 */
@Data
public class GetFileResponse {
    private String id;

    private String cozeId;

    private Date createdTime;

    private String fileName;

    private String fileUrl;
}
