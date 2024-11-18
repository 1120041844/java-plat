package com.work.ai.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.tencentcloudapi.hunyuan.v20230901.models.QueryHunyuanImageJobResponse;
import com.tencentcloudapi.hunyuan.v20230901.models.SubmitHunyuanImageJobResponse;
import com.work.ai.entity.bo.AiDrawDO;
import com.work.ai.entity.dto.CreateImgDTO;
import com.work.ai.entity.vo.CreateImgVO;
import com.work.ai.enums.ImgSizeEnum;
import com.work.ai.enums.ImgStyleEnum;
import com.work.ai.enums.StatusEnum;
import com.work.ai.exception.DataException;
import com.work.ai.mapper.AiDrawMapper;
import com.work.ai.mapper.SysUserRemainingMapper;
import com.work.ai.service.IAIDrawService;
import com.work.ai.utils.HunyuanUtil;
import com.work.ai.utils.SecureUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Slf4j
@Service
public class AIDrawServiceImpl implements IAIDrawService {

    @Autowired
    AiDrawMapper aiDrawMapper;
    @Autowired
    SysUserRemainingMapper sysUserRemainingMapper;

    @Override
    @Transactional
    public CreateImgVO createImgTask(CreateImgDTO createImgDTO) {
        String openId = SecureUtil.getUser().getOpenId();
        // 校验是否有额度
        Long number = sysUserRemainingMapper.selectNumberByOpenId(openId);
        if (number <= 0) {
            throw new DataException("可以额度不足");
        }
        SubmitHunyuanImageJobResponse response = HunyuanUtil.createImg(createImgDTO.getContent(), createImgDTO.getStyleType(), createImgDTO.getSizeType());

        AiDrawDO aiDrawDO = new AiDrawDO();
        aiDrawDO.setOpenId(openId);
        aiDrawDO.setJobId(response.getJobId());
        aiDrawDO.setRequestId(response.getRequestId());
        aiDrawDO.setContent(createImgDTO.getContent());
        aiDrawDO.setStyle(ImgStyleEnum.getStyle(createImgDTO.getStyleType()));
        aiDrawDO.setSize(ImgSizeEnum.getResolution(createImgDTO.getSizeType()));
        aiDrawDO.setCreateTime(new Date());
        aiDrawMapper.insert(aiDrawDO);
        // 扣减额度
        sysUserRemainingMapper.deductionNumber(openId,1);
        CreateImgVO createImgVO = BeanUtil.copyProperties(createImgDTO, CreateImgVO.class);
        createImgVO.setStatus(StatusEnum.process.getCode());
        return createImgVO;
    }

    @Override
    public CreateImgVO getTaskResult(Long id) {
        AiDrawDO aiDrawDO = aiDrawMapper.selectById(id);
        if (aiDrawDO == null) {
            throw new DataException("没找到绘画任务");
        }

        QueryHunyuanImageJobResponse drawResult = HunyuanUtil.getDrawResult(aiDrawDO.getJobId());

        String jobStatusCode = drawResult.getJobStatusCode();
        // 成功
        if ("5".equals(jobStatusCode)) {
            aiDrawDO.setStatus(StatusEnum.success.getCode());
            String[] resultImage = drawResult.getResultImage();
            String url = resultImage[0];
            aiDrawDO.setUrl(url);
            aiDrawMapper.updateById(aiDrawDO);
            // 失败
        } else if ("4".equals(jobStatusCode)) {
            aiDrawDO.setStatus(StatusEnum.fail.getCode());
            String jobErrorMsg = drawResult.getJobErrorMsg();
            aiDrawDO.setErrorMessage(jobErrorMsg);
            aiDrawMapper.updateById(aiDrawDO);
        }

        return BeanUtil.copyProperties(aiDrawDO, CreateImgVO.class);
    }


}
