package com.work.qrcode.controller.code;

import com.work.qrcode.constants.ApiResult;
import com.work.qrcode.controller.BaseController;
import com.work.qrcode.entity.dto.code.CreateCodeDTO;
import com.work.qrcode.enums.BarCodeFormatEnum;
import com.work.qrcode.service.ICodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/code")
public class CodeController extends BaseController {

    @Autowired
    ICodeService codeService;

    @RequestMapping(value = "/create",method = RequestMethod.POST)
    public ApiResult createCode(@RequestBody CreateCodeDTO createCodeDTO) {
        codeService.createCode(createCodeDTO,response);
        return ApiResult.success();
    }

    @RequestMapping(value = "/analyze",method = RequestMethod.POST)
    public ApiResult analyzeCode(MultipartFile file) {
        return ApiResult.data(codeService.analyzeCode(file));
    }

    @RequestMapping(value = "/format/list")
    public ApiResult<List<Map<String,Object>>> getCodeFormat() {
        List<Map<String,Object>> list = new ArrayList<>();
        BarCodeFormatEnum[] values = BarCodeFormatEnum.values();
        for (BarCodeFormatEnum value : values) {
            Map<String,Object> map = new HashMap<>();
            String name = value.name();
            map.put("code",name);
            list.add(map);
        }
        return ApiResult.data(list);
    }
}
