package com.work.plat.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;


@Data
@TableName("sys_user")
public class User {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    @TableId(value = "id")
    private Integer id;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("昵称")
    private String nickname;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("电话")
    private String phone;

    @ApiModelProperty("地址")
    private String address;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("头像")
    private String avatarUrl;

    @ApiModelProperty("角色")
    private String role;

    @TableField(exist = false)
    private List<Course> courses;

    @TableField(exist = false)
    private List<Course> stuCourses;

}
