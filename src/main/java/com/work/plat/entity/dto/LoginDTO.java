package com.work.plat.entity.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class LoginDTO implements Serializable {

    private String username;
    private String phone;
    private String password;
    private String nickname;

}
