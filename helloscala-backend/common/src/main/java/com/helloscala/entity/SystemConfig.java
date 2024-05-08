package com.helloscala.entity;

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
@Schema(name="SystemConfig对象", description="系统配置表")
@TableName("b_system_config")
public class SystemConfig implements Serializable {

    private static final long serialVersionUID=1L;

    @Schema(name = "主键")
      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(name = "七牛云公钥")
    private String qiNiuAccessKey;

    @Schema(name = "七牛云私钥")
    private String qiNiuSecretKey;

    @Schema(name = "七牛云上传空间")
    private String qiNiuBucket;

    @Schema(name = "七牛云存储区域 华东（z0），华北(z1)，华南(z2)，北美(na0)，东南亚(as0)")
    private String qiNiuArea;

    @Schema(name = "七牛云域名前缀")
    private String qiNiuPictureBaseUrl;

    @Schema(name = "是否开启邮件通知(0:否， 1:是)")
    private String startEmailNotification;

    @Schema(name = "是否开启仪表盘通知(0:否， 1:是)")
    private String openDashboardNotification;

    @Schema(name = "仪表盘通知【用于首次登录弹框】")
    private String dashboardNotification;

    @Schema(name = "仪表盘通知【用于首次登录弹框】MD")
    private String dashboardNotificationMd;

    @Schema(name = "搜索模式【0:SQL搜索 、1：全文检索】")
    private int searchModel;

    @Schema(name = "是否开启注册用户邮件激活(0:否， 1:是)")
    private String openEmailActivate;

    @Schema(name = "文件上传七牛云(0:否， 1:是)")
    private String uploadQiNiu;

    @Schema(name = "邮箱地址")
    private String emailHost;
    @Schema(name = "邮箱发件人")
    private String emailUsername;
    @Schema(name = "邮箱授权码")
    private String emailPassword;
    @Schema(name = "邮箱端口")
    private int emailPort;
    @Schema(name = "启用邮箱发送")
    private int openEmail;

    @Schema(name = "本地文件地址")
    private String localFileUrl;
    @Schema(name = "文件上传方式 0:本地 1：七牛云")
    private int fileUploadWay;

    @Schema(name = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @Schema(name = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @Schema(name = "阿里云AccessKey")
    private String aliYunAccessKey;
    @Schema(name = "阿里云SecretKey")
    private String aliYunSecretKey;
    @Schema(name = "阿里云Bucket名称")
    private String aliYunBucket;
    @Schema(name = "阿里云Endpoint")
    private String aliYunEndpoint;

}
