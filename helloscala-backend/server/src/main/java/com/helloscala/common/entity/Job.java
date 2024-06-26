package com.helloscala.common.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.helloscala.common.utils.DateUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;


@Data
@EqualsAndHashCode(callSuper = false)
@Schema(name = "Job对象", description = "定时任务调度表")
@TableName("b_job")
public class Job implements Serializable {
    @Schema(name = "任务ID")
    @TableId(value = "job_id", type = IdType.AUTO)
    private Long jobId;

    @Schema(name = "任务名称")
    private String jobName;

    @Schema(name = "任务组名")
    private String jobGroup;

    @Schema(name = "调用目标字符串")
    private String invokeTarget;

    @Schema(name = "cron执行表达式")
    private String cronExpression;

    @Schema(name = "计划执行错误策略（1立即执行 2执行一次 3放弃执行）")
    private String misfirePolicy;

    @Schema(name = "是否并发执行（0允许 1禁止）")
    private String concurrent;

    @Schema(name = "状态（0正常 1暂停）")
    private String status;

    @Schema(name = "创建者")
    private String createBy;

    @Schema(name = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = DateUtil.FORMAT_STRING,timezone="GMT+8")
    private Date createTime;

    @Schema(name = "更新者")
    private String updateBy;

    @Schema(name = "更新时间")
    @TableField(fill = FieldFill.UPDATE)
    @JsonFormat(pattern = DateUtil.FORMAT_STRING,timezone="GMT+8")
    private Date updateTime;

    @Schema(name = "备注信息")
    private String remark;

    @TableField(exist = false)
    @JsonFormat(pattern = DateUtil.FORMAT_STRING,timezone="GMT+8")
    private Date nextValidTime;
}
