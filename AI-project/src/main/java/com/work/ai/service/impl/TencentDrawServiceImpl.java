package com.work.ai.service.impl;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.io.FileUtil;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.hunyuan.v20230901.HunyuanClient;
import com.tencentcloudapi.hunyuan.v20230901.models.QueryHunyuanImageJobRequest;
import com.tencentcloudapi.hunyuan.v20230901.models.QueryHunyuanImageJobResponse;
import com.tencentcloudapi.hunyuan.v20230901.models.SubmitHunyuanImageJobRequest;
import com.tencentcloudapi.hunyuan.v20230901.models.SubmitHunyuanImageJobResponse;
import com.work.ai.config.CommonClientConfig;
import com.work.ai.config.OssService;
import com.work.ai.entity.bo.AiDrawDO;
import com.work.ai.enums.ApiEnum;
import com.work.ai.enums.ImgSizeEnum;
import com.work.ai.enums.ImgStyleEnum;
import com.work.ai.enums.StatusEnum;
import com.work.ai.mapper.AiDrawMapper;
import com.work.ai.service.DrawService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

@Slf4j
@Service
public class TencentDrawServiceImpl implements DrawService {

    @Autowired
    CommonClientConfig commonClientConfig;
    @Autowired
    AiDrawMapper aiDrawMapper;
    @Autowired
    OssService ossService;

    @Override
    public Integer getType() {
        return ApiEnum.tencent.getCode();
    }

    @Override
    public AiDrawDO createDrawTask(AiDrawDO aiDrawDO) {
        HunyuanClient client = commonClientConfig.getHunyuanClient();
        try {
            SubmitHunyuanImageJobRequest req = new SubmitHunyuanImageJobRequest();
            req.setLogoAdd(0L);
            req.setPrompt(aiDrawDO.getContent());
            req.setStyle(ImgStyleEnum.getStyle(aiDrawDO.getStyle()));
            req.setResolution(ImgSizeEnum.getResolution(aiDrawDO.getSize()));
            SubmitHunyuanImageJobResponse response = client.SubmitHunyuanImageJob(req);

            aiDrawDO.setJobId(response.getJobId());
            aiDrawDO.setRequestId(response.getRequestId());
            aiDrawDO.setApiCode(ApiEnum.tencent.getCode());
            aiDrawMapper.updateById(aiDrawDO);
            return aiDrawDO;
        } catch (TencentCloudSDKException e) {
            aiDrawDO.setStatus(StatusEnum.fail.getCode());
            aiDrawDO.setErrorMessage(ExceptionUtil.getMessage(e));
            aiDrawMapper.updateById(aiDrawDO);
            return aiDrawDO;
        }
    }

    @Override
    public AiDrawDO selectDrawResult(AiDrawDO aiDrawDO) {
        try {
            HunyuanClient client = commonClientConfig.getHunyuanClient();
            QueryHunyuanImageJobRequest req = new QueryHunyuanImageJobRequest();
            req.setJobId(aiDrawDO.getJobId());
            QueryHunyuanImageJobResponse response = client.QueryHunyuanImageJob(req);

            String jobStatusCode = response.getJobStatusCode();
            // 成功
            if ("5".equals(jobStatusCode)) {
                aiDrawDO.setStatus(StatusEnum.success.getCode());
                String[] resultImage = response.getResultImage();
                String url = resultImage[0];
                String uploadOss = uploadOss(url);
                aiDrawDO.setUrl(uploadOss);
                aiDrawMapper.updateById(aiDrawDO);
                // 失败
            } else if ("4".equals(jobStatusCode)) {
                aiDrawDO.setStatus(StatusEnum.fail.getCode());
                String jobErrorMsg = response.getJobErrorMsg();
                aiDrawDO.setErrorMessage(jobErrorMsg);
                aiDrawMapper.updateById(aiDrawDO);
            }
            return aiDrawDO;
        } catch (TencentCloudSDKException e) {
            aiDrawDO.setStatus(StatusEnum.fail.getCode());
            aiDrawDO.setErrorMessage(ExceptionUtil.getMessage(e));
            aiDrawMapper.updateById(aiDrawDO);
            return aiDrawDO;
        }
    }

    private String uploadOss(String fileUrl) {
        try {
            URL url = new URL(fileUrl);
            InputStream inputStream = url.openStream();
            return ossService.putObject("picture", UUID.randomUUID() +".png", inputStream);
        } catch (Exception e) {
            log.error("上传文件失败:",e);
            return null;
        }
    }

}
