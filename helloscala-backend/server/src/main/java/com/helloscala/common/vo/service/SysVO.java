package com.helloscala.common.vo.service;

import lombok.Data;

import java.io.Serializable;

@Data
public class SysVO implements Serializable {
    private String computerName;

    private String computerIp;

    private String userDir;

    private String osName;

    private String osArch;
}
