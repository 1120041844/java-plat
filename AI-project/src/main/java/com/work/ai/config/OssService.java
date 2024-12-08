package com.work.ai.config;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.model.Bucket;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.UUID;

@Service
public class OssService {

    @Autowired
    CommonClientConfig commonClientConfig;

    private static String bucketName = "bucket-1304034978";
    private static String ossUrl = "https://" + bucketName + ".cos.ap-shanghai.myqcloud.com/";


    public void getBucketList() {
        COSClient cosClient = commonClientConfig.getCosClient();
        List<Bucket> buckets = cosClient.listBuckets();
        System.out.println(buckets);

    }

    public String putObject(String fileUrl) throws IOException {
        URL url = new URL(fileUrl);
        InputStream inputStream = url.openStream();
        return putObject("picture", UUID.randomUUID() + ".png", inputStream);

    }


    public String putObject(String folder, String fileName, InputStream inputStream) {
        COSClient cosClient = commonClientConfig.getCosClient();
        if (!folder.endsWith("/")) {
            folder += "/";
        }
        String key = folder + fileName;
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, inputStream, new ObjectMetadata());
        PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
        return ossUrl + key;
    }


    public String putObject(String key, File file) {
        COSClient cosClient = commonClientConfig.getCosClient();
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, file);
        PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
        return ossUrl + key;
    }


}
