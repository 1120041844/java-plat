package com.work.plat.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Author: xjn
 * @Date: 2022/12/5
 */
@Data
@TableName("user")
public class User {

    @TableId("id")
    private String id;

    @TableField("name")
    private String name;

    @TableField("username")
    private String username;

    @TableField("password")
    private String password;

    @TableField("active")
    private String active;
    
}
