package com.work.ai.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ImgSizeEnum {

    SQUARE(1,"正方形","1024:1024","512","512"),
    SIZE_4_3(2,"4:3","1024:768","512","384"),
    SIZE_3_4(3,"3:4","768:1024","384","512"),

    ;

    private Integer type;
    private String desc;
    private String resolution;
    private String width;
    private String height;

    public static String getResolution(Integer type) {
        for (ImgSizeEnum value : values()) {
            if (value.type == type) {
                return value.getResolution();
            }
        }
        return SQUARE.getResolution();
    }

    public static ImgSizeEnum getSize(Integer type) {
        for (ImgSizeEnum value : values()) {
            if (value.type == type) {
                return value;
            }
        }
        return SQUARE;
    }
}
