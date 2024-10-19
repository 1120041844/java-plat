package com.work.plat.service.impl;

import com.work.plat.exception.DataException;
import com.work.plat.service.IOcrService;
import com.work.plat.utils.FileUtils;
import com.work.plat.utils.OcrUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
public class OcrServiceImpl implements IOcrService {
    @Override
    public String ocrRecognition(MultipartFile file) {
        return "ok";
//        try {
//            String base64 = FileUtils.convertToBase64(file);
//            String oCRText = OcrUtils.generalBasicOCR(base64);
//            return oCRText;
//        } catch (Exception e) {
//            throw new DataException("识别失败");
//        }

    }
}
