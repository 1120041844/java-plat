package com.work.plat.entity.bo;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * (sys_order)实体类
 *
 * @author kancy
 * @since 2024-10-22 20:59:21
 * @description 由 Mybatisplus Code Generator 创建
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("sys_order")
public class SysOrderDO implements Serializable {
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
     * orderId
     */
    private String orderId;
    /**
     * payMethod
     */
    private String payMethod;
    /**
     * amount
     */
    private String amount;
    /**
     * payTime
     */
    private Date payTime;
    /**
     * createTime
     */
    private Date createTime;

}
