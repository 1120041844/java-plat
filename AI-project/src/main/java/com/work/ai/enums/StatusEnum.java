package com.work.ai.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StatusEnum {

    process(0,"处理中"),
    success(1,"处理成功"),
    fail(2,"处理失败"),
    ;

    private Integer code;

    private String desc;

    public static String getDesc(Integer code) {
        for (StatusEnum value : values()) {
            if (value.getCode() == code) {
                return value.getDesc();
            }
        }
        return null;
    }

}
