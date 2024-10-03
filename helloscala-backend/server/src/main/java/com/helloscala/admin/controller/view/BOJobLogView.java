package com.helloscala.admin.controller.view;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.helloscala.common.utils.DateUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
public class BOJobLogView {
    @Schema(name = "任务日志ID")
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
    @JsonFormat(pattern = DateUtil.FORMAT_STRING, timezone = "GMT+8")
    private Date createTime;

    @JsonFormat(pattern = DateUtil.FORMAT_STRING, timezone = "GMT+8")
    private Date startTime;

    @JsonFormat(pattern = DateUtil.FORMAT_STRING, timezone = "GMT+8")
    private Date stopTime;
}
