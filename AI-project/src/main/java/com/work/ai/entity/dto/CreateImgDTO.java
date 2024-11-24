package com.work.ai.entity.dto;

import lombok.Data;

import java.io.Serializable;
@Data
public class CreateImgDTO implements Serializable {

    private String content;

    private Integer size;

    private Integer style;
}
