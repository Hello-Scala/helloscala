package com.helloscala.common.vo.tag;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.helloscala.common.utils.DateUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SystemTagListVo {
    @Schema(name = "主键id")
    private String id;

    @Schema(name = "标签名称")
    private String name;

    @Schema(name = "封面图")
    private String avatar;

    @Schema(name = "排序")
    private int sort;

    @Schema(name = "点击量")
    private int clickVolume;

    @Schema(name = "创建时间")
    @JsonFormat(pattern = DateUtil.FORMAT_STRING, timezone = "GMT+8")
    private Date createTime;

    @Schema(name = "最后更新时间")
    @JsonFormat(pattern = DateUtil.FORMAT_STRING, timezone = "GMT+8")
    private Date updateTime;

    @Schema(name = "文章量")
    private int articleCount;
}
