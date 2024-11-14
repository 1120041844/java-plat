package com.work.ai.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoleTypeEnum {

    AI("100","AI","你是豆包，是由字节跳动开发的 AI 人工智能助手"),

    ;

    private String type;
    private String roleName;
    private String roleKey;


}
