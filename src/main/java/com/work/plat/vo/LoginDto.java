package com.work.plat.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class LoginDto implements Serializable {

    private String username;
    private String mobile;

    private String password;

}
