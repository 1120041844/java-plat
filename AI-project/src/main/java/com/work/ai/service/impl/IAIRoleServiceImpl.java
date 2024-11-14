package com.work.ai.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.work.ai.entity.bo.AiRoleDO;
import com.work.ai.entity.vo.AIRoleVO;
import com.work.ai.mapper.AiRoleMapper;
import com.work.ai.service.IAIRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IAIRoleServiceImpl implements IAIRoleService {

    @Autowired
    AiRoleMapper aiRoleMapper;

    @Override
    public List<AIRoleVO> list() {
        List<AiRoleDO> aiRoleDOS = aiRoleMapper.selectList(new QueryWrapper<>());
        return BeanUtil.copyToList(aiRoleDOS,AIRoleVO.class);
    }
}
