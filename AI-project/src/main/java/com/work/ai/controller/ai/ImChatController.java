package com.work.ai.controller.ai;

import com.work.ai.constants.ApiResult;
import com.work.ai.entity.vo.ImChatVO;
import com.work.ai.service.IImChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/imChat")
public class ImChatController {

    @Autowired
    IImChatService imChatService;

    @GetMapping("/getHistory")
    public ApiResult<List<ImChatVO>> getHistory(@RequestParam("openId") String openId, @RequestParam(required = false, value = "type")String type) {
        return ApiResult.data(imChatService.getHistory(openId,type));
    }

}
