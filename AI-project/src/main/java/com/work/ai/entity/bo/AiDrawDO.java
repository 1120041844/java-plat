package com.work.ai.entity.bo;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * (ai_draw)实体类
 *
 * @author kancy
 * @since 2024-11-18 21:40:46
 * @description 由 Mybatisplus Code Generator 创建
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("ai_draw")
public class AiDrawDO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId
	private Long id;
    /**
     * openId
     */
    private String openId;
    /**
     * jobId
     */
    private String jobId;
    /**
     * requestId
     */
    private String requestId;
    /**
     * content
     */
    private String content;
    /**
     * style
     */
    private String style;
    /**
     * size
     */
    private String size;

    private Integer status;

    private String errorMessage;
    /**
     * url
     */
    private String url;
    /**
     * token
     */
    private Integer token;
    /**
     * createTime
     */
    private Date createTime;

}
