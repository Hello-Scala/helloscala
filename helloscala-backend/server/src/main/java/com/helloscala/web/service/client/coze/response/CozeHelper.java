package com.helloscala.web.service.client.coze.response;

import java.util.Date;

/**
 * @author Steve Zou
 */
public class CozeHelper {
    public static Date toDate(long seconds) {
        return new Date(seconds * 1000);
    }
}
