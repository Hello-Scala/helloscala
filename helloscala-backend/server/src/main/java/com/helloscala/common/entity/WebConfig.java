package com.helloscala.common.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;


@Data
@EqualsAndHashCode(callSuper = false)
@Schema(name="TWebConfig对象", description="网站配置表")
@TableName("b_web_config")
public class WebConfig implements Serializable {
    @Schema(name = "主键")
      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(name = "logo(文件UID)")
    private String logo;

    @Schema(name = "网站名称")
    private String name;

    @Schema(name = "网站地址")
    private String webUrl;

    @Schema(name = "介绍")
    private String summary;

    @Schema(name = "关键字")
    private String keyword;

    @Schema(name = "作者")
    private String author;

    @Schema(name = "备案号")
    private String recordNum;

    @Schema(name = "创建时间")
      @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @Schema(name = "更新时间")
      @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    @Schema(name = "支付宝收款码FileId")
    private String aliPay;

    @Schema(name = "微信收款码FileId")
    private String weixinPay;

    @Schema(name = "github地址")
    private String github;

    @Schema(name = "gitee地址")
    private String gitee;

    @Schema(name = "QQ号")
    private String qqNumber;

    @Schema(name = "QQ群")
    private String qqGroup;

    @Schema(name = "邮箱")
    private String email;

    @Schema(name = "微信")
    private String wechat;

    @Schema(name = "显示的列表（用于控制邮箱、QQ、QQ群、Github、Gitee、微信是否显示在前端）")
    private String showList;

    @Schema(name = "登录方式列表（用于控制前端登录方式，如账号密码,码云,Github,QQ,微信）")
    private String loginTypeList;

    @Schema(name = "是否开启评论(0:否 1:是)")
    private Integer openComment;

    @Schema(name = "是否开启赞赏(0:否， 1:是)")
    private Integer openAdmiration;

    @Schema(name = "作者简介")
    private String authorInfo;

    @Schema(name = "作者头像")
    private String authorAvatar;

    @Schema(name = "游客头像")
    private String touristAvatar;

    @Schema(name = "公告")
    private String bulletin;

    @Schema(name = "是否显示公告  0 不显示 1显示")
    private Integer showBulletin;

    @Schema(name = "关于我")
    private String aboutMe;

    @Schema(name = "是否开启音乐播放器")
    private Integer isMusicPlayer;
}
