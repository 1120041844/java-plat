package com.work.qrcode.entity.dto.user;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserDTO implements Serializable {
    private Integer id;
    private String openId;
    private String username;
    private String phone;
    private String nickname;
    private String avatar;

}
