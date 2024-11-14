package com.work.qrcode.exception;


import com.work.qrcode.constants.IResultCode;

public class BaseException extends RuntimeException {

    private Integer code;

    public BaseException() {
    }

    public BaseException(Integer code, String message) {
        super(message);
        this.code=code;
    }

    public BaseException(IResultCode resultCode) {
        super(resultCode.getMessage());
        this.code=resultCode.getCode();
    }

    public BaseException(Integer code, String message, Throwable throwable) {
        super(message,throwable);
        this.code=code;
    }

    public Integer getCode(){
        return this.code;
    }
}

