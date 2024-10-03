package com.helloscala.service.web.view;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.helloscala.common.utils.DateUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
public class JobLogView {
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
    private Date createTime;

    private Date startTime;

    private Date stopTime;
}
