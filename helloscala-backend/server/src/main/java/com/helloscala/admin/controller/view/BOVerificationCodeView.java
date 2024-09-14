package com.helloscala.admin.controller.view;

import lombok.Data;

/**
 * @author stevezou
 */
@Data
public class BOVerificationCodeView {
    private String captchaKey;

    private String captchaBase64;
}
