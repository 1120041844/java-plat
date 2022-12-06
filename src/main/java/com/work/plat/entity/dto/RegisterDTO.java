package com.work.plat.entity.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class RegisterDTO implements Serializable {

    private String name;

    private String nickName;

    private String username;

    private String mobile;

    private String password;

}
