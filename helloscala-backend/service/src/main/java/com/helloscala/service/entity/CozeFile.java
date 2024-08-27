package com.helloscala.service.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.helloscala.common.utils.DateUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * @author Steve Zou
 */

@Data
@TableName("b_coze_file")
@Schema(name = "上传到Coze的文件", description = "上传到Coze的文件")
public class CozeFile {
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    private String cozeId;

    private String fileName;

    private String fileUrl;

    private Integer bytes;

    private String userId;

    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = DateUtil.FORMAT_STRING, timezone = "GMT+8")
    private Date createTime;

    private String createBy;
}
