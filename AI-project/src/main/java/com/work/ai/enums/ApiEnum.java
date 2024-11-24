package com.work.ai.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ApiEnum {

    tencent(1,"混元"),
    doubao(2,"豆包"),

    ;

    private Integer code;

    private String desc;

}
