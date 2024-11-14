package com.work.ai.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class AuthInfoDTO implements Serializable {
    @ApiModelProperty(value = "令牌")
    private String token;
    @ApiModelProperty(value = "过期时间")
    private long expiresIn;
    @ApiModelProperty(value = "刷新令牌")
    private String refreshToken;
    @ApiModelProperty(value = "用户")
    private UserDTO userDTO;
}
