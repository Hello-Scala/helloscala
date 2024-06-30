package com.helloscala.common.vo.article;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.helloscala.common.entity.Tag;
import com.helloscala.common.utils.DateUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiArticleListVO {

    @Schema(name = "主键id")
    private Long id;

    @Schema(name = "作者昵称")
    private String nickname;

    @Schema(name = "作者头像")
    private String userAvatar;

    @Schema(name = "作者id")
    private String userId;

    @Schema(name = "文章标题")
    private String title;

    @Schema(name = "文章封面图")
    private String avatar;

    @Schema(name = "文章描述")
    private String summary;

    @Schema(name = "文章内容")
    private String content;

    @Schema(name = "是否置顶")
    private Integer isStick;

    @Schema(name = "是否原创")
    private Integer isOriginal;

    @Schema(name = "是否发布")
    private Integer isPublish;

    @Schema(name = "阅读量")
    private Integer quantity;

    @Schema(name = "评论数")
    private Integer commentCount;

    @Schema(name = "点赞量")
    private Object likeCount;

    @Schema(name = "收藏量")
    private int collectCount;

    @Schema(name = "当前用户是否收藏")
    private Boolean isCollect;

    @Schema(name = "我的评论内容")
    private String commentContent;

    @Schema(name = "分类名")
    private String categoryName;

    @Schema(name = "分类id")
    private String categoryId;

    @Schema(name = "创建时间")
    @JsonFormat(pattern = DateUtil.FORMAT_STRING,timezone="GMT+8")
    private Date createTime;

    @Schema(name = "格式化后的创建时间")
    private String formatCreateTime;

    @Schema(name = "标签集合")
    private List<Tag> tagList;
}
