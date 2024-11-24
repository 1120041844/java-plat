package com.work.ai.entity.vo;

import lombok.Data;

import java.io.Serializable;
@Data
public class AiDrawStyleVO implements Serializable {

    private Long id;
    /**
     * type
     */
    private Integer type;
    /**
     * desc
     */
    private String style;
    /**
     * url
     */
    private String url;
}
