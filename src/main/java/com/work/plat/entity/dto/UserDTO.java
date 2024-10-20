package com.work.plat.entity.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class UserDTO implements Serializable {
    private Integer id;
    private String openId;
    private String username;
    private String phone;
    private String nickname;
    private String avatar;

}
