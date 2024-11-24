package com.work.ai.utils;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.SSEResponseModel;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.hunyuan.v20230901.HunyuanClient;
import com.tencentcloudapi.hunyuan.v20230901.models.*;
import com.work.ai.enums.ImgSizeEnum;
import com.work.ai.enums.ImgStyleEnum;

import java.util.Iterator;

public class HunyuanUtil {

    private static String secretId = "AKIDlMo47au3ziEZieNtEMHzUonZ8aqshJj1";
    private static String secretKey = "A7xb2GHtKYZO2TikZDfuz781oPp35vXy";
    private static HunyuanClient client;
    static  {
        client= new HunyuanClient(new Credential(secretId,secretKey),"ap-guangzhou");
    }



    public static void getToken() throws TencentCloudSDKException {

        GetTokenCountRequest req = new GetTokenCountRequest();
        req.setPrompt("你好,给我讲个笑话");
        GetTokenCountResponse count = client.GetTokenCount(req);
        Long tokenCount = count.getTokenCount();
        System.out.println(tokenCount);

    }

    public static void chatStream() throws TencentCloudSDKException {

        ChatCompletionsRequest req = new ChatCompletionsRequest();
        req.setModel("hunyuan-pro");
        req.setStream(true);
        Message[] messages = new Message[1];
        Message message = new Message();
        message.setRole("user");
        message.setContent("给我讲个笑话");
        messages[0] = message;
        req.setMessages(messages);
        ChatCompletionsResponse response = client.ChatCompletions(req);

        Iterator<SSEResponseModel.SSE> iterator = response.iterator();
        while (iterator.hasNext()) {
            SSEResponseModel.SSE next = iterator.next();
            String data = next.Data;
            JSONObject jsonObject = JSONObject.parseObject(data);
            String content = jsonObject.getJSONArray("Choices").getJSONObject(0).getJSONObject("Delta").getString("Content");
            System.out.print(content);
        }

    }

    public static SubmitHunyuanImageJobResponse createImg(String content, Integer style, Integer size) {
        try {
            SubmitHunyuanImageJobRequest req = new SubmitHunyuanImageJobRequest();
            req.setLogoAdd(0L);
            req.setPrompt(content);
            req.setStyle(ImgStyleEnum.getStyle(style));
            req.setResolution(ImgSizeEnum.getResolution(size));
            SubmitHunyuanImageJobResponse response = client.SubmitHunyuanImageJob(req);
            return response;
        } catch (TencentCloudSDKException e) {

        }
        return null;
    }

    public static QueryHunyuanImageJobResponse getDrawResult(String jobId) {
        try {
            QueryHunyuanImageJobRequest req = new QueryHunyuanImageJobRequest();
            req.setJobId(jobId);
            QueryHunyuanImageJobResponse response = client.QueryHunyuanImageJob(req);
            return response;
        } catch (TencentCloudSDKException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) throws TencentCloudSDKException {

        QueryHunyuanImageJobResponse drawResult = getDrawResult("1304034978-1732351011-15ede649-a976-11ef-a7a3-5254005d40ea-0");
        System.out.println(drawResult.getResultImage()[0]);
    }

}
