package com.work.qrcode.entity.vo;

import lombok.Data;

import java.io.Serializable;
@Data
public class CodeVO implements Serializable {

    private String content;

    private String format;
}
