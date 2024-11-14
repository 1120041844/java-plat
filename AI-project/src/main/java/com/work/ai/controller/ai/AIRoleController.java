package com.work.ai.controller.ai;

import com.work.ai.controller.base.BaseController;
import com.work.ai.entity.vo.AIRoleVO;
import com.work.ai.service.IAIRoleService;
import com.work.ai.constants.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/aiRole")
public class AIRoleController extends BaseController {

    @Autowired
    IAIRoleService iaiRoleService;

    @GetMapping("/list")
    public ApiResult<List<AIRoleVO>> list() {
        return ApiResult.data(iaiRoleService.list());
    }
}
