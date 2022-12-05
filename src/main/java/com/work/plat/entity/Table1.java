package com.work.plat.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@TableName("table1")
public class Table1 implements Serializable {

    private static final long serialVersionUID = 1L;


    private Integer id;

    private String field1;

    private String field2;

    private String field3;

    private String field4;

    private String field5;

    private String field6;

    private String createId;

    private String createName;

    private LocalDateTime createDate;

    private String updateId;

    private String updateName;

    private LocalDateTime updateDate;

    @TableLogic
    private Integer active;


}
