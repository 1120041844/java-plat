package com.work.plat.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;


@Data
@TableName("user")
public class User {

    @TableId("id")
    private String id;

    @TableField("name")
    private String name;

    @TableField("nick_name")
    private String nickName;

    @TableField("username")
    private String username;

    @TableField("mobile")
    private String mobile;

    @TableField("password")
    private String password;

    @TableField("create_id")
    private String createId;
    @TableField("create_name")
    private String createName;
    @TableField(value = "create_date",fill = FieldFill.INSERT)
    private Date createDate;

    @TableField("update_id")
    private String updateId;
    @TableField("update_name")
    private String updateName;
    @TableField(value = "update_date",fill = FieldFill.INSERT)
    private Date updateDate;

    @TableLogic
    @TableField(value = "active",fill = FieldFill.INSERT)
    private Integer active;
    
}
