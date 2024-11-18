package com.work.ai.controller.ai;

import com.work.ai.constants.ApiResult;
import com.work.ai.controller.base.BaseController;
import com.work.ai.entity.dto.CreateImgDTO;
import com.work.ai.entity.IdDTO;
import com.work.ai.service.IAIDrawService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/draw")
public class AIDrawController extends BaseController {

    @Resource
    IAIDrawService iaiDrawService;

    @PostMapping("/createImgTask")
    public ApiResult createImgTask(@RequestBody CreateImgDTO createImgDTO) {
        return ApiResult.data(iaiDrawService.createImgTask(createImgDTO));
    }

    @PostMapping("/getTaskResult")
    public ApiResult getTaskResult(@RequestBody IdDTO IdDTO) {
        return ApiResult.data(iaiDrawService.getTaskResult(IdDTO.getId()));
    }
}
