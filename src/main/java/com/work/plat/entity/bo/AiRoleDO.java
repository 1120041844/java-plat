package com.work.plat.entity.bo;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * (ai_role)实体类
 *
 * @author kancy
 * @since 2024-10-26 11:25:16
 * @description 由 Mybatisplus Code Generator 创建
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("ai_role")
public class AiRoleDO  implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId
	private Long id;
    /**
     * title
     */
    private String title;
    /**
     * desc
     */
    private String description;
    /**
     * hello
     */
    private String hello;
    /**
     * roleKey
     */
    private String roleKey;
    /**
     * params
     */
    private String path;
    /**
     * type
     */
    private String type;

    private Integer click;

    private Integer good;
    /**
     * createTime
     */
    private Date createTime;

}
