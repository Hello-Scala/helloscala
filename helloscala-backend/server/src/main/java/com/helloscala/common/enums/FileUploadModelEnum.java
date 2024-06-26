package com.helloscala.common.enums;

public enum FileUploadModelEnum {
    /**
     * 本地上传
     */
    LOCAL(0, "本地", "localFileStrategyImpl"),
    /**
     * 七牛云上传
     */
    QIN(1, "七牛云", "qiNiuFileStrategyImpl"),
    FTP(3, "FTP", "ftpFileStrategyImpl"),
    /**
     * 阿里云上传
     */
    ALI(2, "阿里云", "aliFileStrategyImpl");

    //创建构造函数
    FileUploadModelEnum(int code, String desc, String strategy) {
        this.type = code;
        this.desc = desc;
        this.strategy = strategy;
    }


    /**
     * 上传方式
     */
    private final Integer type;
    /**
     * 描述
     */
    private final String desc;
    /**
     * 策略
     */
    private final String strategy;

    public Integer getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }

    public String getStrategy() {
        return strategy;
    }

    /**
     * 获取策略
     *
     * @param type 模式
     * @return {@link String} 上传策略
     */
    public static FileUploadModelEnum getStrategy(int type) {
        for (FileUploadModelEnum value : FileUploadModelEnum.values()) {
            if (value.getType() == type) {
                return value;
            }
        }
        return null;
    }
}
