package com.helloscala.common.vo.article;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.helloscala.common.utils.DateUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiArchiveVO {
    private Long id;

    private String title;

    private String time;

    @JsonFormat(pattern = DateUtil.MM_DD,timezone="GMT+8")
    private Date formatTime;
}
