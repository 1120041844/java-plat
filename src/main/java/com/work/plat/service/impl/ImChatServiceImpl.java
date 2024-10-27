package com.work.plat.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.work.plat.entity.bo.ImChatDO;
import com.work.plat.entity.vo.ImChatVO;
import com.work.plat.mapper.ImChatMapper;
import com.work.plat.service.IImChatService;
import lombok.extern.slf4j.Slf4j;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
