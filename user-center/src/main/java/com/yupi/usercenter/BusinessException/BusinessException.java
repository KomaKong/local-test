package com.yupi.usercenter.BusinessException;

import com.yupi.usercenter.commmons.ErrorCode;

public class BusinessException extends RuntimeException {

    private final int code;

    private final String detail;

    public BusinessException(String message,int code,String detail) {
        super(message);
        this.code = code;
        this.detail = detail;
    }

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.detail = errorCode.getDetail();
    }

    public BusinessException(ErrorCode errorCode,String detail) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.detail = detail;
    }

    public int getCode() {
        return code;
    }

    public String getDetail() {
        return detail;
    }
}
