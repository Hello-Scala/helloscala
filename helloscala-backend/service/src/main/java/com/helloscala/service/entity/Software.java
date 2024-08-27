package com.helloscala.service.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName(value = "b_software")
public class Software {
    @TableId
    private Integer id;

    private String name;

    private String info;

    private String codeUrl;

    private String coverImg;

    private String demoUrl;

    private String detailUrl;

    private Date createTime;
}