package com.work.ai.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.work.ai.entity.bo.AiDrawDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (ai_draw)数据Mapper
 *
 * @author kancy
 * @since 2024-11-18 21:40:46
 * @description 由 Mybatisplus Code Generator 创建
*/
@Mapper
public interface AiDrawMapper extends BaseMapper<AiDrawDO> {


    AiDrawDO selectByJobId(@Param("openId")String openId, @Param("jobId")String jobId);

    List<AiDrawDO> selectByOpenId(@Param("openId")String openId);


}
