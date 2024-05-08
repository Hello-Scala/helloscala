package com.helloscala.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.helloscala.utils.DateUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;


@Data
@EqualsAndHashCode(callSuper = false)
@Schema(name="JobLog对象", description="定时任务调度日志表")
@TableName("b_job_log")
public class JobLog implements Serializable {

    private static final long serialVersionUID=1L;

    @Schema(name = "任务日志ID")
      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(name = "任务ID")
    private Long jobId;

    @Schema(name = "任务名称")
    private String jobName;

    @Schema(name = "任务组名")
    private String jobGroup;

    @Schema(name = "调用目标字符串")
    private String invokeTarget;

    @Schema(name = "日志信息")
    private String jobMessage;

    @Schema(name = "执行状态（0正常 1失败）")
    private String status;

    @Schema(name = "异常信息")
    private String exceptionInfo;

    @Schema(name = "创建时间")
      @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = DateUtil.FORMAT_STRING, timezone = "GMT+8")
    private Date createTime;

    /** 开始时间 */
    @JsonFormat(pattern = DateUtil.FORMAT_STRING, timezone = "GMT+8")
    private Date startTime;
    /** 停止时间 */
    @JsonFormat(pattern = DateUtil.FORMAT_STRING, timezone = "GMT+8")
    private Date stopTime;
}
