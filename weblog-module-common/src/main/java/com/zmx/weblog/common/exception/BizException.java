package com.zmx.weblog.common.exception;


import com.zmx.weblog.common.enums.ResponseCodeEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BizException extends RuntimeException{
    //异常码
    private String errorCode;
    //异常信息
    private String errorMessage;
    public BizException(ResponseCodeEnum baseExceptionInterface){
        this.errorCode = baseExceptionInterface.getErrorCode();
        this.errorMessage = baseExceptionInterface.getErrorMessage();
    }

    public BizException(BaseExceptionInterface baseExceptionInterface){
        this.errorCode = baseExceptionInterface.getErrorCode();
        this.errorMessage = baseExceptionInterface.getErrorMessage();
    }
}
