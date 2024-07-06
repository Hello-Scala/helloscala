package com.helloscala.common.vo.user;

import lombok.Data;

/**
 * @author Steve Zou
 */
@Data
public class UserCountView {
    private Long articleCount;
    private Long collectCount;
    private Long followedCount;
}
