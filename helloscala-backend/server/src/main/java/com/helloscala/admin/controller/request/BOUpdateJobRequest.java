package com.helloscala.admin.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class BOUpdateJobRequest {
    @Schema(name = "任务ID")
    private String jobId;

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

    @Schema(name = "备注信息")
    private String remark;
}
