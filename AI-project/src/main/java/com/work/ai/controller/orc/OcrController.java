package com.work.ai.controller.orc;

import com.work.ai.controller.base.BaseController;
import com.work.ai.constants.ApiResult;
import com.work.ai.service.IOcrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/ocr")
public class OcrController extends BaseController {

    @Autowired
    IOcrService ocrService;

    @PostMapping("/recognition")
    public ApiResult<String> ocrRecognition(MultipartFile file) {
        return ApiResult.success(ocrService.ocrRecognition(file));
    }
}
