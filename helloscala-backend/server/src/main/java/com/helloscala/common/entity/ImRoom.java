package com.helloscala.common.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.helloscala.common.utils.DateUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@TableName("b_im_room")
@Schema(name="ImRoom", description="")
public class ImRoom implements Serializable {
    @Schema(name = "主键id")
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;


    @Schema(name = "房间类型 0：群聊 1私聊")
    private Integer type;

    @Schema(name = "发送用户id")
    private String fromUserId;

    @Schema(name = "接收用户id")
    private String toUserId;


    @Schema(name = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = DateUtil.FORMAT_STRING,timezone="GMT+8")
    private Date createTime;
}
