package com.work.ai.config;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.region.Region;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.hunyuan.v20230901.HunyuanClient;
import com.volcengine.ark.runtime.service.ArkService;
import com.work.ai.constants.CommonConstant;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonClientConfig {

    @Bean
    public HunyuanClient getHunyuanClient() {
        Credential credential = new Credential(CommonConstant.tencentSecretId, CommonConstant.tencentSecretKey);
        return new HunyuanClient(credential,"ap-guangzhou");
    }

    @Bean
    public ArkService getDoubaoClient() {
        ArkService service = ArkService.builder().apiKey(CommonConstant.doubaoApiKey).build();
        return service;
    }

    @Bean
    public COSClient getCosClient() {
        COSCredentials cred = new BasicCOSCredentials(CommonConstant.tencentSecretId, CommonConstant.tencentSecretKey);
        ClientConfig commonClientConfig = new ClientConfig(new Region("ap-shanghai"));
        COSClient cosClient = new COSClient(cred, commonClientConfig);
        return cosClient;
    }

}
