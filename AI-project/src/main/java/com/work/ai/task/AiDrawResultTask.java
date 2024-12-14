package com.work.ai.task;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.work.ai.entity.bo.AiDrawDO;
import com.work.ai.entity.bo.SysUserRemainingDO;
import com.work.ai.enums.ApiEnum;
import com.work.ai.enums.StatusEnum;
import com.work.ai.mapper.AiDrawMapper;
import com.work.ai.mapper.SysUserRemainingMapper;
import com.work.ai.service.IAIDrawService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;


@Slf4j
@Component
public class AiDrawResultTask {

    @Autowired
    AiDrawMapper aiDrawMapper;
    @Autowired
    IAIDrawService iaiDrawService;
    @Autowired
    SysUserRemainingMapper sysUserRemainingMapper;

    @Scheduled(cron ="*/2 * * * * ?")
    public void selectDrawResult() {
        LambdaQueryWrapper<AiDrawDO> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(AiDrawDO::getStatus, StatusEnum.process.getCode());
        List<AiDrawDO> aiDrawDOS = aiDrawMapper.selectList(queryWrapper);
        if (CollUtil.isEmpty(aiDrawDOS)) {
            return;
        }
        for (AiDrawDO aiDrawDO : aiDrawDOS) {
            iaiDrawService.getTaskResult(aiDrawDO);
        }
    }

//    @Scheduled(cron = "0 0 2 * * ?")
//    public void increaseCreditLimit() {
//        List<SysUserRemainingDO> sysUserRemainingDOS = sysUserRemainingMapper.selectList(new LambdaQueryWrapper<>());
//        if (CollUtil.isEmpty(sysUserRemainingDOS)) {
//            return;
//        }
//        for (SysUserRemainingDO remainingDO : sysUserRemainingDOS) {
//            Long number = remainingDO.getNumber();
//            if (number < 50) {
//                Long updatedNumber = Math.min(number + 10, 50L);
//                if (!updatedNumber.equals(number)) {
//                    remainingDO.setNumber(updatedNumber);
//                    sysUserRemainingMapper.updateById(remainingDO);
//                }
//            }
//        }
//    }
}
