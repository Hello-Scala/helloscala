package com.helloscala.common.vo.service;

import lombok.Data;

import java.io.Serializable;

@Data
public class SysFileVO implements Serializable {
    private String dirName;

    private String sysTypeName;

    private String typeName;

    private String total;

    private String free;

    private String used;

    private double usage;
}
