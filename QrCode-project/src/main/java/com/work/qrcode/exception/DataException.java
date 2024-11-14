package com.work.qrcode.exception;


import com.work.qrcode.constants.IResultCode;
import com.work.qrcode.constants.ResultCodeEnum;

public class DataException extends  BaseException {

    public DataException(String message) {
        super(ResultCodeEnum.BUSINESS_ERROR.getCode(), message);
    }

    public DataException(Integer code, String message) {
        super(code, message);
    }

    public DataException(ResultCodeEnum codeEnum) {
        super(codeEnum.getCode(), codeEnum.getMessage());
    }

    public DataException(IResultCode resultCode) {
        super(resultCode);
    }
}
