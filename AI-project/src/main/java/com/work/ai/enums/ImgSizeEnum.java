package com.work.ai.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ImgSizeEnum {

    SQUARE(1,"正方形","1024:1024"),
    SIZE_4_3(2,"正方形","1024:768"),
    SIZE_3_4(3,"正方形","768:1024"),

    ;

    private Integer type;
    private String desc;
    private String resolution;

    public static String getResolution(Integer type) {
        for (ImgSizeEnum value : values()) {
            if (value.type == type) {
                return value.getResolution();
            }
        }
        return SQUARE.getResolution();
    }
}
