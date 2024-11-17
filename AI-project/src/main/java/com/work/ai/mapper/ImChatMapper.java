package com.work.ai.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.work.ai.entity.bo.ImChatDO;
import com.work.ai.entity.vo.ImChatHistoryListVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (im_chat)数据Mapper
 *
 * @author kancy
 * @since 2024-10-22 20:59:21
 * @description 由 Mybatisplus Code Generator 创建
*/
@Mapper
public interface ImChatMapper extends BaseMapper<ImChatDO> {

    List<ImChatHistoryListVO> getHistoryList(@Param("openId")String openId, @Param("type") String type);

    List<ImChatDO> selectMessage(@Param("type")String type, @Param("messageId") String messageId);

    List<ImChatDO> selectHistory(@Param("openId") String openId,@Param("messageId")String messageId, @Param("type")String type);

}
