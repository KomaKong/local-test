package com.yupi.usercenter.commmons;


public class Result {

    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(0,"ok",data,"");
    }

    public  static BaseResponse error(ErrorCode errorCode)
    {
        return new BaseResponse<>(errorCode);
    }

    public  static BaseResponse error(ErrorCode errorCode,String message,String detail)
    {
        return new BaseResponse<>(errorCode.getCode(),errorCode.getMessage(),null,detail);
    }

    public  static BaseResponse error(int errorCode,String message,String detail)
    {
        return new BaseResponse<>(errorCode,message,null,detail);
    }

    public  static BaseResponse error(ErrorCode errorCode,String detail)
    {
        return new BaseResponse<>(errorCode.getCode(),errorCode.getMessage(),null,detail);
    }
}
