package com.work.ai.service.impl;

import com.tencentcloudapi.hunyuan.v20230901.models.QueryHunyuanImageJobResponse;
import com.tencentcloudapi.hunyuan.v20230901.models.SubmitHunyuanImageJobResponse;
import com.work.ai.entity.dto.CreateImgDTO;
import com.work.ai.service.IAIDrawService;
import com.work.ai.utils.HunyuanUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
@Slf4j
@Service
public class AIDrawServiceImpl implements IAIDrawService {


    @Override
    public Object createImg(CreateImgDTO createImgDTO) {
        SubmitHunyuanImageJobResponse response = HunyuanUtil.createImg(createImgDTO.getContent(), createImgDTO.getStyleType(), createImgDTO.getSizeType());

        String jobId = response.getJobId();
        String requestId = response.getRequestId();

        // 入库
        return null;

    }

    @Override
    public Object selectDrawResult(String jobId) {
        QueryHunyuanImageJobResponse drawResult = HunyuanUtil.getDrawResult(jobId);

        String jobStatusCode = drawResult.getJobStatusCode();
        String jobErrorMsg = drawResult.getJobErrorMsg();

        String[] resultImage = drawResult.getResultImage();
        return resultImage[0];


    }


}
