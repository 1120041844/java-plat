package com.work.ai.controller.ai;

import com.work.ai.constants.ApiResult;
import com.work.ai.entity.vo.ImChatHistoryListVO;
import com.work.ai.entity.vo.ImChatVO;
import com.work.ai.service.IImChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/imChat")
public class ImChatController {

    @Autowired
    IImChatService imChatService;

    @GetMapping("/getHistoryList")
    public ApiResult<List<ImChatHistoryListVO>> getHistory( @RequestParam("type")String type) {
        return ApiResult.data(imChatService.getHistoryList(type));
    }

    @GetMapping("/getHistory")
    public ApiResult<List<ImChatVO>> getHistoryDetail(@RequestParam String messageId) {
        return ApiResult.data(imChatService.getHistoryDetail(messageId));
    }

}
