package com.work.plat.controller.ai;

import com.work.plat.constants.ApiResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ai")
public class AiController {

    @PostMapping("/question")
    public ApiResult<String> answer() {
        return ApiResult.success("你好，我是豆包AI");
    }

}
