package com.work.ai.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.work.ai.entity.bo.ImChatDO;
import com.work.ai.entity.vo.ImChatVO;
import com.work.ai.mapper.ImChatMapper;
import com.work.ai.service.IImChatService;
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

    @Override
    public List<ImChatVO> getHistory(String openId, String type) {
        if (StrUtil.isEmpty(openId)) {
            return null;
        }
        List<ImChatDO> imChatDOS = imChatMapper.selectHistory(openId,type);
        return BeanUtil.copyToList(imChatDOS,ImChatVO.class);
    }
}
