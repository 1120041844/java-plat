package com.work.ai.entity.bo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * (im_chat)实体类
 *
 * @author kancy
 * @since 2024-10-22 20:59:21
 * @description 由 Mybatisplus Code Generator 创建
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("im_chat")
public class ImChatDO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
	private Long id;

    private String openId;
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

    private Integer status;

    private String errorMessage;
    /**
     * tokens
     */
    private String tokens;
    /**
     * createDate
     */
    private Date createTime;

}
