package com.helloscala.admin.controller.view;

import lombok.Data;

@Data
public class BOCaptchaView {
    private String captchaKey;

    private String captchaBase64;
}
