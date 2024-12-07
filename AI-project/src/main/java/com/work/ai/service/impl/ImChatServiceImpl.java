package com.work.ai.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.work.ai.entity.bo.AiDrawDO;
import com.work.ai.entity.bo.ImChatDO;
import com.work.ai.entity.dto.CreateImgDTO;
import com.work.ai.entity.vo.ImChatHistoryListVO;
import com.work.ai.entity.vo.ImChatVO;
import com.work.ai.mapper.AiDrawMapper;
import com.work.ai.mapper.ImChatMapper;
import com.work.ai.service.IImChatService;
import com.work.ai.utils.SecureUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * (im_chat)数据DAO
 *
 * @author kancy
 * @since 2024-10-22 20:59:21
 * @description 由 Mybatisplus Code Generator 创建
 */
@Slf4j
@Service
public class ImChatServiceImpl extends ServiceImpl<ImChatMapper, ImChatDO> implements IImChatService {

    @Autowired
    ImChatMapper imChatMapper;
    @Autowired
    AiDrawMapper drawMapper;


    @Override
    public List<ImChatHistoryListVO> getHistoryList(String type) {
        String openId = SecureUtil.getUser().getOpenId();
        if (StrUtil.isEmpty(openId)) {
            return null;
        }
        if ("1".equals(type)) {
            return imChatMapper.getHistoryList(openId);
        } else if ("2".equals(type)){
            List<AiDrawDO> aiDrawDOS = drawMapper.selectByOpenId(openId);
            return BeanUtil.copyToList(aiDrawDOS,ImChatHistoryListVO.class);
        }
        return null;
    }



    @Override
    public List<ImChatVO> getHistoryDetail(String messageId) {
        String openId = SecureUtil.getUser().getOpenId();
        if (StrUtil.isEmpty(openId)) {
            return null;
        }
        List<ImChatDO> imChatDOS = imChatMapper.selectHistory(openId,messageId);
        return BeanUtil.copyToList(imChatDOS,ImChatVO.class);
    }

}
