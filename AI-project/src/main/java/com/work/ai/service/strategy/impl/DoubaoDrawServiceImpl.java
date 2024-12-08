package com.work.ai.service.strategy.impl;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.volcengine.service.visual.IVisualService;
import com.work.ai.config.CommonClientConfig;
import com.work.ai.config.OssService;
import com.work.ai.entity.bo.AiDrawDO;
import com.work.ai.entity.doubao.DoubaoResult;
import com.work.ai.entity.doubao.DrawResultVO;
import com.work.ai.enums.ApiEnum;
import com.work.ai.enums.ImgSizeEnum;
import com.work.ai.enums.ImgStyleEnum;
import com.work.ai.enums.StatusEnum;
import com.work.ai.mapper.AiDrawMapper;
import com.work.ai.service.strategy.DrawService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class DoubaoDrawServiceImpl implements DrawService {

    @Autowired
    CommonClientConfig commonClientConfig;
    @Autowired
    AiDrawMapper aiDrawMapper;
    @Autowired
    OssService ossService;

    @Override
    public Integer getType() {
        return ApiEnum.doubao.getCode();
    }

    @Override
    public AiDrawDO createDrawTask(AiDrawDO aiDrawDO) {
        try {
            IVisualService instance = commonClientConfig.getVisualInstance();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("req_key","high_aes_general_v20_L");
            jsonObject.put("model_version","general_v2.0_L");
            ImgSizeEnum sizeEnum = ImgSizeEnum.getSize(aiDrawDO.getSize());
            jsonObject.put("width",sizeEnum.getWidth());
            jsonObject.put("height",sizeEnum.getHeight());
            String stylePrefix = getImgStylePrefix(aiDrawDO.getStyle());
            jsonObject.put("prompt",stylePrefix + aiDrawDO.getContent());
            Object task = instance.cvSync2AsyncSubmitTask(jsonObject);
            JSONObject response = JSONObject.parseObject(String.valueOf(task));
            Integer code = response.getInteger("code");
            if (code == 10000) {
                JSONObject data = response.getJSONObject("data");
                String task_id = data.getString("task_id");
                aiDrawDO.setJobId(task_id);
            }
            aiDrawDO.setRequestId(response.getString("request_id"));
            aiDrawDO.setApiCode(ApiEnum.doubao.getCode());
            aiDrawMapper.updateById(aiDrawDO);
            return aiDrawDO;
        } catch (Exception e) {
            aiDrawDO.setStatus(StatusEnum.fail.getCode());
            aiDrawDO.setErrorMessage(ExceptionUtil.getMessage(e));
            aiDrawMapper.updateById(aiDrawDO);
            return aiDrawDO;
        }
    }

    private String getImgStylePrefix(Integer style) {
        String desc = ImgStyleEnum.getDesc(style);
        if (StrUtil.isNotEmpty(desc)) {
            return desc +"，";
        }
        return null;
    }

    @Override
    public AiDrawDO selectDrawResult(AiDrawDO aiDrawDO) {
        try {
            String jobId = aiDrawDO.getJobId();
            IVisualService instance = commonClientConfig.getVisualInstance();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("req_key","high_aes_general_v20_L");
            jsonObject.put("task_id",jobId);
            JSONObject req = new JSONObject();
            JSONObject logo = new JSONObject();
            logo.put("add_logo",false);
            req.put("logo_info",logo);
            req.put("return_url",true);
            jsonObject.put("req_json",req.toJSONString());
            Object task = instance.cvSync2AsyncGetResult(jsonObject);
            DoubaoResult doubaoResult = JSONObject.parseObject(String.valueOf(task), DoubaoResult.class);
            boolean success = doubaoResult.isSuccess();
            if (success) {
                DrawResultVO data = JSONObject.parseObject(String.valueOf(doubaoResult.getData()), DrawResultVO.class);
                String status = data.getStatus();
                if ("done".equals(status)) {
                    aiDrawDO.setStatus(StatusEnum.success.getCode());
                    List<String> imageList = data.getImage_urls();
                    String uploadOss = ossService.putObject(imageList.get(0));
                    aiDrawDO.setUrl(uploadOss);
                    aiDrawMapper.updateById(aiDrawDO);
                }
            } else {
                aiDrawDO.setStatus(StatusEnum.fail.getCode());
                String jobErrorMsg = doubaoResult.getMessage();
                aiDrawDO.setErrorMessage(jobErrorMsg);
                aiDrawMapper.updateById(aiDrawDO);
            }
        } catch (Exception e) {
            log.error("获取绘画结果失败:",e);
            aiDrawDO.setStatus(StatusEnum.fail.getCode());
            aiDrawDO.setErrorMessage(ExceptionUtil.getMessage(e));
            aiDrawMapper.updateById(aiDrawDO);
            return aiDrawDO;
        }
        return null;
    }


}
