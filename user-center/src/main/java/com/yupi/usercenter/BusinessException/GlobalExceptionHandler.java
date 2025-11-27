package com.yupi.usercenter.BusinessException;

import com.yupi.usercenter.commmons.BaseResponse;
import com.yupi.usercenter.commmons.ErrorCode;
import com.yupi.usercenter.commmons.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j

public class GlobalExceptionHandler {

    @ExceptionHandler(value = BusinessException.class)
    public BaseResponse businessExceptionHandler(BusinessException e){
        log.error("businessException" + e.getMessage(), e);
        return Result.error(e.getCode(), e.getMessage(),e.getDetail());
    }

    public BaseResponse runtimeExceptionHandler(RuntimeException e){
        log.error("runtimeException:", e);
        return Result.error(ErrorCode.SYSTEM_ERROR,e.getMessage(),"");
    }
}

