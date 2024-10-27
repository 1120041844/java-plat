package com.work.plat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.work.plat.entity.bo.ImChatDO;
import com.work.plat.entity.vo.ImChatVO;

import java.util.List;

public interface IImChatService extends IService<ImChatDO> {
    List<ImChatVO> getHistory(String openId,String type);
}
