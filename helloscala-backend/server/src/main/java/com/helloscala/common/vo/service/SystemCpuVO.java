package com.helloscala.common.vo.service;

import lombok.Data;

import java.io.Serializable;

@Data
public class SystemCpuVO implements Serializable {
    private Integer cpuNum;

    private double total;

    private double sys;

    private double used;

    private double wait;

    private double free;
}