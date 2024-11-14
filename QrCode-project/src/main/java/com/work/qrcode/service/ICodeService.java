package com.work.qrcode.service;

import com.work.qrcode.entity.dto.code.CreateCodeDTO;
import com.work.qrcode.entity.vo.CodeVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

public interface ICodeService {

    void createCode(CreateCodeDTO createCodeDTO, HttpServletResponse response);

    CodeVO analyzeCode(MultipartFile file);
}
