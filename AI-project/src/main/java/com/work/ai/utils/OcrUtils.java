package com.work.ai.utils;

import cn.hutool.core.codec.Base64;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.ocr.v20181119.OcrClient;
import com.tencentcloudapi.ocr.v20181119.models.GeneralBasicOCRRequest;
import com.tencentcloudapi.ocr.v20181119.models.GeneralBasicOCRResponse;
import com.tencentcloudapi.ocr.v20181119.models.TextDetection;

import java.io.File;

public class OcrUtils {

    private static String appId = "1304034978";
    private static String secretId = "AKIDlMo47au3ziEZieNtEMHzUonZ8aqshJj1";
    private static String secretKey = "A7xb2GHtKYZO2TikZDfuz781oPp35vXy";

//    private static SecretId = "AKIDxoSt8AyZwPUO7dvzrY1MTO6btxpNktrb";
//    private static SecretKey ="aw74rkndFfrx3wRq34ziGcnmZ9XkkXr2";

    private static OcrClient ocrClient;
    static {
        Credential credential = new Credential(secretId, secretKey);
        ocrClient = new OcrClient(credential, "ap-shanghai");
    }

    public static void main(String[] args) throws TencentCloudSDKException {
        File file = new File("/Users/jianan/Pictures/WechatIMG137.jpg");
        String encode = Base64.encode(file);
        String ocr = OcrUtils.generalBasicOCR(encode);
        System.out.println(ocr);

    }

    public static String generalBasicOCR(String base64) throws TencentCloudSDKException {
        GeneralBasicOCRRequest req = new GeneralBasicOCRRequest();
        req.setIsPdf(true);
        req.setImageBase64(base64);

        GeneralBasicOCRResponse generalBasicOCR = ocrClient.GeneralBasicOCR(req);
        TextDetection[] textDetections = generalBasicOCR.getTextDetections();
        StringBuilder sb = new StringBuilder();
        for (TextDetection detection : textDetections) {
            String text = detection.getDetectedText();
            sb.append(text);
        }
        return sb.toString();
    }
}
