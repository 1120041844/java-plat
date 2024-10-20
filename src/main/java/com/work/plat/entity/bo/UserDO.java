package com.work.plat.entity.bo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.util.Date;


@Data
@TableName("sys_user")
public class UserDO {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    private String openId;

    private String username;

    private String password;

    private String nickname;

    private String email;

    private String phone;

    private String address;

    private String avatar;

    private Date createTime;


}
