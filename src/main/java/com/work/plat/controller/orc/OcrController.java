package com.work.plat.controller.orc;

import com.work.plat.constants.ApiResult;
import com.work.plat.controller.base.BaseController;
import com.work.plat.service.IOcrService;
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
