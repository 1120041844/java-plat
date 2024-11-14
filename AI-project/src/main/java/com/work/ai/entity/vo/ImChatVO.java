package com.work.ai.entity.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class ImChatVO implements Serializable {

    private Long id;
    /**
     * messageId
     */
    private String messageId;
    /**
     * shortId
     */
    private String shortId;
    /**
     * question
     */
    private String question;
    /**
     * answer
     */
    private String answer;

    private String type;

}
