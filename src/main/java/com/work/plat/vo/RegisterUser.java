package com.work.plat.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class RegisterUser implements Serializable {

    private String name;

    private String nickName;

    private String username;

    private String mobile;

    private String password;

}
