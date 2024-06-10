package com.helloscala.common.vo.article;

import com.helloscala.common.entity.Category;
import com.helloscala.common.entity.Tags;
import com.helloscala.common.vo.message.ApiCommentListVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ApiArticleInfoVO {

    @Schema(name = "主键id")
    private Long id;

    @Schema(name = "用户id")
    private String userId;

    @Schema(name = "所属用户名")
    private String nickname;

    @Schema(name = "文章标题")
    private String title;

    @Schema(name = "作者头像")
    private String avatar;

    @Schema(name = "文章简介")
    private String summary;
    @Schema(name = "文章内容")
    private String content;

    @Schema(name = "文章内容MD版")
    private String contentMd;

    @Schema(name = "是否置顶 0否 1是")
    private Integer isStick;

    @Schema(name = "发布状态")
    private Integer isPublish;

    @Schema(name = "是否原创  0：转载 1:原创")
    private Integer isOriginal;

    @Schema(name = "转载地址")
    private String originalUrl;

    @Schema(name = "关键词")
    private String keywords;

    @Schema(name = "发布地址")
    private String address;

    @Schema(name = "阅读方式 0无需验证 1：评论阅读 2：点赞阅读 3：扫码阅读")
    private Integer readType;

    @Schema(name = "文章阅读量")
    private Integer quantity;


    @Schema(name = "点赞量")
    private Object likeCount;

    @Schema(name = "评论量")
    private Integer commentCount;

    @Schema(name = "发表时间")
    private Date createTime;

    @Schema(name = "修改时间")
    private Date updateTime;

    @Schema(name = "标签集合")
    private List<Tags> tagList;

    @Schema(name = "分类")
    private Category category;

    @Schema(name = "当前用户是否点赞")
    private Boolean isLike = false;

    @Schema(name = "当前用户是否收藏")
    private int isCollect;

    @Schema(name = "当前用户是否关注作者")
    private int isFollowed;

    @Schema(name = "收藏量")
    private int collectCount;

    @Schema(name = "当前用户激活阅读方式 如已评论或已点赞或已扫码验证过")
    private Boolean activeReadType = false;

    @Schema(name = "评论")
    private List<ApiCommentListVO> apiCommentListVos;
}
