package com.work.ai.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 状态码，参照阿里5位状态码编码规范
 * 00000：成功
 * A：用户错误,枚举CLIENT开头
 * B：系统错误，枚举SERVER开头
 * C：第三方错误
 */
@Getter
@AllArgsConstructor
public enum ResultCodeEnum implements IResultCode  {
    SUCCESS(200, "成功"),
    FAIL(555, "失败"),

    NOT_TOKEN(401, "没有token信息，请登录"),
    NOT_FOUND(404, "没有找到页面"),


    BUSINESS_ERROR(1001004, "业务逻辑异常"),


            ;

    /**
     * code编码
     */
    final Integer code;
    /**
     * 中文信息描述
     */
    final String message;
}
