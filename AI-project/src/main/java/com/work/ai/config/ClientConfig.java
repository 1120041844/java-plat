package com.work.ai.config;

import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.hunyuan.v20230901.HunyuanClient;
import com.volcengine.ark.runtime.service.ArkService;
import com.work.ai.constants.CommonConstant;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientConfig {

    @Bean
    public HunyuanClient getTencentClient() {
        Credential credential = new Credential(CommonConstant.tencentSecretId, CommonConstant.tencentSecretKey);
        return new HunyuanClient(credential,"ap-guangzhou");
    }

    @Bean
    public ArkService getDoubaoClient() {
        ArkService service = ArkService.builder().apiKey(CommonConstant.doubaoApiKey).build();
        return service;
    }


}
