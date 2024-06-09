package com.helloscala.common;



public enum ResultCode {
    //成功
    SUCCESS( 200, "成功" ),
    //失败
    FAILURE( 400, "失败" ),


    // 系统级别错误码
    ERROR(-1, "Operation error!"),
    ERROR_DEFAULT(500,"Server error, please try it later!"),
    NOT_LOGIN(401, "Login timeout"),
    NO_PERMISSION(-7,"No permission"),
    ERROR_PASSWORD(-8,"User account or password incorrect!"),
    DISABLE_ACCOUNT(-12,"Account disabled!"),


    // 服务层面
    ERROR_EXCEPTION_MOBILE_CODE(10003,"Verification code invalid or expired, please check!"),
    ERROR_USER_NOT_EXIST(10009, "User not found!"),
    PARAMS_ILLEGAL(10018,"Param illegal !"),
    CATEGORY_IS_EXIST(10019,"Category exist!"),
    CATEGORY_IS_TOP(10020,"Category is top!"),
    TAG_EXIST(10021,"Tag exist!"),
    FETCH_ARTICLE_FAILED(10022,"Fetch article failed!"),
    ARTICLE_NOT_FOUND(10023,"Article not found!");

    public int code;
    public String desc;

    ResultCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
