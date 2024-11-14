package com.work.ai.entity.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class LoginDTO implements Serializable {

    private String username;
    private String phone;
    private String password;
    private String nickname;
    private String avatar;

}
