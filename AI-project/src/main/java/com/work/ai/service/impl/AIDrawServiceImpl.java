package com.work.ai.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.work.ai.constants.GlobalLock;
import com.work.ai.entity.bo.AiDrawDO;
import com.work.ai.entity.bo.AiDrawStyleDO;
import com.work.ai.entity.dto.CreateImgDTO;
import com.work.ai.entity.vo.AiDrawStyleVO;
import com.work.ai.entity.vo.CreateImgVO;
import com.work.ai.enums.ApiEnum;
import com.work.ai.enums.StatusEnum;
import com.work.ai.exception.DataException;
import com.work.ai.mapper.AiDrawMapper;
import com.work.ai.mapper.AiDrawStyleMapper;
import com.work.ai.mapper.SysUserRemainingMapper;
import com.work.ai.service.strategy.DrawFactory;
import com.work.ai.service.strategy.DrawService;
import com.work.ai.service.IAIDrawService;
import com.work.ai.utils.SecureUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
@Service
public class AIDrawServiceImpl implements IAIDrawService {

    @Autowired
    AiDrawMapper aiDrawMapper;
    @Autowired
    SysUserRemainingMapper sysUserRemainingMapper;
    @Autowired
    AiDrawStyleMapper aiDrawStyleMapper;

    @Override
    @Transactional
    public CreateImgVO createImgTask(CreateImgDTO createImgDTO) {
        ReentrantLock doubaoLock = GlobalLock.getDoubaoLock();
        try {
            boolean lock = doubaoLock.tryLock(30, TimeUnit.SECONDS);
            if (!lock) {
                throw new DataException("系统繁忙，请稍后重试。");
            }
            String openId = SecureUtil.getUser().getOpenId();
            // 校验是否有额度
            Long number = sysUserRemainingMapper.selectNumberByOpenId(openId);
            if (number <= 0) {
                throw new DataException("可用额度不足");
            }
            AiDrawDO aiDrawDO = new AiDrawDO();
            aiDrawDO.setOpenId(openId);
            aiDrawDO.setContent(createImgDTO.getContent());
            aiDrawDO.setStyle(createImgDTO.getStyle());
            aiDrawDO.setSize(createImgDTO.getSize());
            aiDrawDO.setCreateTime(new Date());
            aiDrawMapper.insert(aiDrawDO);

            DrawService service = DrawFactory.getService(ApiEnum.doubao.getCode());
            service.createDrawTask(aiDrawDO);
            // 扣减额度
            sysUserRemainingMapper.deductionNumber(openId,1);
            CreateImgVO createImgVO = BeanUtil.copyProperties(createImgDTO, CreateImgVO.class);
            createImgVO.setStatus(StatusEnum.process.getCode());
            return createImgVO;
        } catch (DataException e) {
            log.error("异常:",e);
            throw e;
        } catch (InterruptedException e) {
            log.error("异常:",e);
            throw new DataException("系统繁忙，请稍后重试。");
        } finally {
            doubaoLock.unlock();
        }
    }

    @Override
    public CreateImgVO getTaskResult(Long id) {
        AiDrawDO aiDrawDO = aiDrawMapper.selectById(id);
        if (aiDrawDO == null) {
            throw new DataException("没找到绘画任务");
        }

        return getTaskResult(aiDrawDO);
    }

    @Override
    public CreateImgVO getTaskResult(AiDrawDO aiDrawDO) {
        ReentrantLock doubaoLock = GlobalLock.getDoubaoLock();
        try {
            boolean lock = doubaoLock.tryLock(30, TimeUnit.SECONDS);
            if (!lock) {
                throw new DataException("系统繁忙，请稍后重试。");
            }
            Integer apiCode = aiDrawDO.getApiCode();
            if (apiCode == null) {
                log.info("apiCode为空：{}",aiDrawDO.getId());
                return null;
            }
            DrawService service = DrawFactory.getService(apiCode);
            aiDrawDO = service.selectDrawResult(aiDrawDO);
            return BeanUtil.copyProperties(aiDrawDO, CreateImgVO.class);
        } catch (DataException e) {
            log.error("异常:",e);
            throw e;
        } catch (InterruptedException e) {
            log.error("异常:",e);
            throw new DataException("系统繁忙，请稍后重试。");
        } finally {
            doubaoLock.unlock();
        }
    }

    @Override
    public List<AiDrawStyleVO> getDrawStyle() {
        List<AiDrawStyleDO> aiDrawStyleDOS = aiDrawStyleMapper.selectList(Wrappers.emptyWrapper());
        return BeanUtil.copyToList(aiDrawStyleDOS, AiDrawStyleVO.class);
    }


}
