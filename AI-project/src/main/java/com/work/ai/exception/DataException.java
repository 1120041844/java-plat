package com.work.ai.exception;


import com.work.ai.constants.IResultCode;
import com.work.ai.constants.ResultCodeEnum;

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
