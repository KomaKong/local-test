package com.yupi.usercenter.commmons;

import lombok.Data;
import org.apache.ibatis.jdbc.Null;

public enum  ErrorCode {

    SUSSCE(200,"OK",""),
    PARMAS_ERROR(4000,"请求参数错误",""),
    NULL_ERROR(4001,"请求参数为空",""),
    NOT_LOGIN_ERROR(40100,"未登录",""),
    NOAUTH_ERROR(40101,"无权限",""),
    SYSTEM_ERROR(5000,"系统异常",""),;


    private int code;
    private String message;
    private String detail;
    ErrorCode(int code, String message, String detail) {
        this.code = code;
        this.message = message;
        this.detail = detail;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getDetail() {
        return detail;
    }
}
