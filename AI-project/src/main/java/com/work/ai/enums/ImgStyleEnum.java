package com.work.ai.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ImgStyleEnum {

    riman(1,"日漫动画"),
    shuimo(2,"水墨画"),
    monai(3,"莫奈"),
    bianping(4,"扁平插画"),
    xiangsu(5,"像素插画"),
    ertonghuiben(6,"儿童绘本"),
    xuanran3d(7,"3D 渲染"),
    manhua(8,"漫画"),
    heibaimanhua(9,"黑白漫画"),
    xieshi(10,"写实"),
    dongman(11,"动漫"),
    bijiasuo(12,"毕加索"),
    saibopengke(13,"赛博朋克"),
    youhua(14,"油画"),
    masaike(15,"马赛克"),
    qinghuaci(16,"青花瓷"),
    xinnianjianzhi(17,"新年剪纸画"),
    xinnianhuayi(18,"新年花艺"),

    ;

    private Integer type;
    private String desc;


    public static String getStyle(Integer type) {
        if (type == null) return null;

        for (ImgStyleEnum value : values()) {
            if (value.type == type) {
                return value.name();
            }
        }
        return null;
    }

    public static String getDesc(Integer type) {
        if (type == null) return null;

        for (ImgStyleEnum value : values()) {
            if (value.type == type) {
                return value.getDesc();
            }
        }
        return null;
    }
}
