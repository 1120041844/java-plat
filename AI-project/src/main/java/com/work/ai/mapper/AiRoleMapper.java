package com.work.ai.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.work.ai.entity.bo.AiRoleDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


/**
 * (ai_role)数据Mapper
 *
 * @author kancy
 * @since 2024-10-26 11:25:16
 * @description 由 Mybatisplus Code Generator 创建
*/
@Mapper
public interface AiRoleMapper extends BaseMapper<AiRoleDO> {


    String getRoleKey(@Param("type") String type);
}
