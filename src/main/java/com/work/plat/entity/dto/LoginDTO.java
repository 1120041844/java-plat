package com.work.plat.entity.dto;

import com.work.plat.entity.Menu;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class LoginDTO implements Serializable {

    private Integer id;
    private String username;
    private String password;
    private String nickname;
    private String avatarUrl;
    private String token;
    private String role;
    private List<Menu> menus;

}
