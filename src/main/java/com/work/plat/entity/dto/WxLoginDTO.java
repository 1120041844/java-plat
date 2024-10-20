package com.work.plat.entity.dto;

import lombok.Data;

import java.io.Serializable;
@Data
public class WxLoginDTO implements Serializable {

    private String code;
    private String encData;
    private String iv;
    private LoginDTO loginDTO;
}
