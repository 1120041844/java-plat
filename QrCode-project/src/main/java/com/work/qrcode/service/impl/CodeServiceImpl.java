package com.work.qrcode.service.impl;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.work.qrcode.entity.dto.code.CreateCodeDTO;
import com.work.qrcode.entity.vo.CodeVO;
import com.work.qrcode.enums.BarCodeFormatEnum;
import com.work.qrcode.service.ICodeService;
import com.work.qrcode.utils.BarcodeGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@Slf4j
@Service
public class CodeServiceImpl implements ICodeService {

    @Override
    public void createCode(CreateCodeDTO createCodeDTO, HttpServletResponse response) {
        // 获取用户

        // 校验内容
        try {
            Integer codeType = createCodeDTO.getCodeType();
            String content = createCodeDTO.getContent();
            Boolean showText = createCodeDTO.getShowText();
            String format = createCodeDTO.getFormat();
            BarCodeFormatEnum formatEnum = BarCodeFormatEnum.getFormat(codeType, format);
            ServletOutputStream outputStream = response.getOutputStream();
            BarcodeGenerator.createCode(codeType, showText, content, formatEnum,outputStream);

        } catch (IOException e) {
            log.error("条码生成失败:",e);
        }
    }

    @Override
    public CodeVO analyzeCode(MultipartFile file) {
        CodeVO codeVO = new CodeVO();
        try {
            Result result = BarcodeGenerator.analyzeCode(file);
            codeVO.setContent(result.getText());
            BarcodeFormat format = result.getBarcodeFormat();
            codeVO.setFormat(BarCodeFormatEnum.getFormat(format).name());
        } catch (Exception e) {
            log.error("解析图片失败:",e);
        }
        return codeVO;
    }
}
