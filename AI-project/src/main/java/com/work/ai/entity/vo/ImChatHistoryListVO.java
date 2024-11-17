package com.work.ai.entity.vo;

import lombok.Data;

import java.io.Serializable;
@Data
public class ImChatHistoryListVO implements Serializable {

    private String question;

    private String messageId;

    private String type;

}
