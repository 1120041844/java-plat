package com.work.qrcode.constants;

import java.io.Serializable;

public interface IResultCode extends Serializable {
    /**
     * 消息
     *
     * @return String
     */
    String getMessage();

    /**
     * 状态码
     *
     * @return Integer
     */
    Integer getCode();
}
