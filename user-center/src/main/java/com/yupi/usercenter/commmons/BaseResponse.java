package com.yupi.usercenter.commmons;

import lombok.Data;

import java.io.Serializable;

@Data
public class BaseResponse<T> implements Serializable {

    private static final long serialVersionUID = 1L;
    private int code;
    private String messsage;
    private T data;
    private String detail;

    public BaseResponse(int code, String messsage, T data, String detail) {
        this.code = code;
        this.messsage = messsage;
        this.data = data;
        this.detail = detail;
    }

    public BaseResponse(int code, T data) {
        this(code,"",data,"");
    }

    public BaseResponse(ErrorCode errorCode) {
        this(errorCode.getCode(),errorCode.getMessage(),null, errorCode.getDetail());
    }

}
