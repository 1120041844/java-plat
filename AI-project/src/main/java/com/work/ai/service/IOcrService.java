package com.work.ai.service;

import org.springframework.web.multipart.MultipartFile;

public interface IOcrService {

    String ocrRecognition(MultipartFile file);
}
