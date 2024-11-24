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
 * (ai_draw_style)实体类
 *
 * @author kancy
 * @since 2024-11-23 22:27:09
 * @description 由 Mybatisplus Code Generator 创建
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("ai_draw_style")
public class AiDrawStyleDO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId
	private Long id;
    /**
     * type
     */
    private Integer type;
    /**
     * desc
     */
    private String style;
    /**
     * url
     */
    private String url;
    /**
     * ramark
     */
    private String remark;
    /**
     * createTime
     */
    private Date createTime;

}
