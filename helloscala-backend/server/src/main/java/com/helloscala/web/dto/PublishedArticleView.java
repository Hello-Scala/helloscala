package com.helloscala.web.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.helloscala.common.utils.DateUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * @author Steve Zou
 */
@Data
public class PublishedArticleView {
    @Schema(name = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(name = "用户id")
    private String userId;

    @Schema(name = "分类id")
    private Long categoryId;

    @Schema(name = "文章标题")
    private String title;

    @Schema(name = "文章封面地址")
    private String avatar;

    @Schema(name = "文章简介")
    private String summary;

    @Schema(name = "阅读方式 0无需验证 1：评论阅读 2：点赞阅读 3：扫码阅读")
    private Integer readType;

    @Schema(name = "是否置顶 0否 1是")
    private Integer isStick;

    @Schema(name = "是否原创 0：转载 1:原创")
    private Integer isOriginal;

    @Schema(name = "转发地址")
    private String originalUrl;

    @Schema(name = "关键词")
    private String keywords;

    @Schema(name = "发布地址")
    private String address;

    @Schema(name = "文章阅读量")
    private Integer quantity;

    @Schema(name = "是否首页轮播")
    private Integer isCarousel;

    @Schema(name = "是否推荐 0否 1是")
    private Integer isRecommend;

    @Schema(name = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = DateUtil.FORMAT_STRING, timezone = "GMT+8")
    private Date createTime;

    @Schema(name = "创建月份和日期")
    @JsonFormat(pattern = DateUtil.MM_DD, timezone = "GMT+8")
    private Date createMonthlyDate;

    @Schema(name = "最后更新时间")
    @TableField(fill = FieldFill.UPDATE)
    @JsonFormat(pattern = DateUtil.FORMAT_STRING, timezone = "GMT+8")
    private Date updateTime;
}
