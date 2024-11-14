package com.work.ai.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.work.ai.entity.vo.ImChatVO;
import com.work.ai.entity.bo.ImChatDO;

import java.util.List;

public interface IImChatService extends IService<ImChatDO> {
    List<ImChatVO> getHistory(String openId, String type);
}
