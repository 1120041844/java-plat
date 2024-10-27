package com.work.plat.controller.ai;

import com.work.plat.constants.ApiResult;
import com.work.plat.entity.vo.ImChatVO;
import com.work.plat.service.IImChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
