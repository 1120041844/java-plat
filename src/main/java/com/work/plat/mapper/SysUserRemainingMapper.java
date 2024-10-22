package com.work.plat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.work.plat.entity.bo.SysUserRemainingDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * (sys_user_remaining)数据Mapper
 *
 * @author kancy
 * @since 2024-10-22 21:19:28
 * @description 由 Mybatisplus Code Generator 创建
*/
@Mapper
public interface SysUserRemainingMapper extends BaseMapper<SysUserRemainingDO> {


    Long selectNumberByOpenId(@Param("openId")String openId);

    void deductionNumber(@Param("openId")String openId ,@Param("times") Integer times);
}
