package com.work.ai.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.work.ai.entity.dto.CreateImgDTO;
import com.work.ai.entity.vo.ImChatHistoryListVO;
import com.work.ai.entity.vo.ImChatVO;
import com.work.ai.entity.bo.ImChatDO;

import java.util.List;

public interface IImChatService extends IService<ImChatDO> {
    List<ImChatVO> getHistory(String openId,String messageId, String type);

    List<ImChatHistoryListVO> getHistoryList(String openId, String type);

    Object createImg(CreateImgDTO createImgDTO);
}
