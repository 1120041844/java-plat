package com.work.plat.entity.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

@Data
public class AIRoleVO implements Serializable {

    /**
     * id
     */
    private Long id;
    /**
     * title
     */
    private String title;
    /**
     * desc
     */
    private String description;

    private String hello;
    /**
     * params
     */
    private String path;
    /**
     * type
     */
    private String type;

    private Integer click;

    private Integer good;

}
