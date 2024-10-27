package com.work.plat.controller.ai;

import com.work.plat.constants.ApiResult;
import com.work.plat.controller.base.BaseController;
import com.work.plat.entity.vo.AIRoleVO;
import com.work.plat.service.IAIRoleService;
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
