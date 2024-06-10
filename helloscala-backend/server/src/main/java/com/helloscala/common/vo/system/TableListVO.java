package com.helloscala.common.vo.system;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.helloscala.common.utils.DateUtil;
import lombok.Data;

import java.util.Date;

@Data
public class TableListVO {

    private String  name;

    private String  comment;

    @JsonFormat(pattern = DateUtil.FORMAT_STRING,timezone="GMT+8")
    private Date  createTime;

    @JsonFormat(pattern = DateUtil.FORMAT_STRING,timezone="GMT+8")
    private Date updateTime;
}
