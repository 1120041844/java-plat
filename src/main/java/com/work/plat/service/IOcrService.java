package com.work.plat.service;

import org.springframework.web.multipart.MultipartFile;

public interface IOcrService {

    String ocrRecognition(MultipartFile file);
}
