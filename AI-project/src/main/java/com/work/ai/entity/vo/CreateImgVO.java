package com.work.ai.entity.vo;

import com.work.ai.enums.StatusEnum;
import lombok.Data;

import java.io.Serializable;

@Data
public class CreateImgVO implements Serializable {

    private Long id;

    private String content;

    private Integer sizeType;

    private Integer styleType;

    private Integer status;

    private String statusDesc;

    private String url;

    public String getStatusDesc() {
        if (status != null) {
            return StatusEnum.getDesc(status);
        }
        return null;
    }
}
