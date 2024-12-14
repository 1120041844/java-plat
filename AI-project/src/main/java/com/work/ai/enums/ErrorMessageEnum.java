package com.work.ai.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorMessageEnum {
    CODE_DEFAULT(55555,"生成失败"),

    CODE_50500(50500,"生成失败"),
    CODE_50411(50411,"图片审核未通过"),
    CODE_50511(50511,"图片审核未通过"),
    CODE_50412(50412,"图片审核未通过"),
    CODE_50512(50512,"图片审核未通过"),
    CODE_50413(50413,"生成失败"),

    ;


    private Integer code;

    private String message;

    public static String getMessage(Integer code) {
        for (ErrorMessageEnum value : values()) {
            if (value.getCode().equals(code)) {
                return value.getMessage();
            }
        }
        return CODE_DEFAULT.getMessage();
    }
}
