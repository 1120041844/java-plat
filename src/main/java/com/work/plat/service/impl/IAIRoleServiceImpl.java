package com.work.plat.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.work.plat.entity.bo.AiRoleDO;
import com.work.plat.entity.vo.AIRoleVO;
import com.work.plat.mapper.AiRoleMapper;
import com.work.plat.service.IAIRoleService;
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
