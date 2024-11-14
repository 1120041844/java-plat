package com.work.qrcode.entity.dto.code;

import lombok.Data;

import java.io.Serializable;
@Data
public class CreateCodeDTO implements Serializable {

    private Integer codeType;

    private String content;

    private String remark;

    private Boolean showText;

    private String format;
}
