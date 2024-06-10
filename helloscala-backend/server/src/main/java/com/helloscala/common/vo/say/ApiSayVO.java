package com.helloscala.common.vo.say;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.helloscala.common.utils.DateUtil;
import com.helloscala.common.vo.user.UserInfoVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ApiSayVO {
    @Schema(name = "主键id")
    private String id;

    @Schema(name = "用户Id")
    private String userId;

    @Schema(name = "用户昵称")
    private String nickname;

    @Schema(name = "用户头像")
    private String avatar;


    @Schema(name = "图片地址 逗号分隔  最多九张")
    private String imgUrl;

    @Schema(name = "内容")
    private String content;

    @Schema(name = "发表地址。可输入或者地图插件选择")
    private String address;

    @Schema(name = "是否开放查看  0未开放 1开放")
    private Integer isPublic;

    @Schema(name = "点赞用户")
    private List<UserInfoVO> userLikeList;

    @Schema(name = "是否点赞")
    private Boolean isLike;

    @Schema(name = "评论集合")
    private List<ApiSayCommentVO> sayCommentVOList;


    @Schema(name = "创建时间")
    @JsonFormat(pattern = DateUtil.FORMAT_STRING,timezone="GMT+8")
    private Date createTime;

    @Schema(name = "创建时间")
    private String createTimeStr;
}
