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
 * (sys_user_remaining)实体类
 *
 * @author kancy
 * @since 2024-10-22 21:19:28
 * @description 由 Mybatisplus Code Generator 创建
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("sys_user_remaining")
public class SysUserRemainingDO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
	private Long id;
    /**
     * openId
     */
    private String openId;
    /**
     * number
     */
    private Long number;
    /**
     * createTime
     */
    private Date createTime;

}
