package com.work.plat.controller.user;

import com.work.plat.constants.ApiResult;
import com.work.plat.constants.TokenConstant;
import com.work.plat.controller.base.BaseController;
import com.work.plat.entity.dto.AuthInfoDTO;
import com.work.plat.entity.dto.LoginDTO;
import com.work.plat.entity.dto.UserPasswordDTO;
import com.work.plat.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: xjn
 * @Date: 2022/12/5
 */
@RestController
@RequestMapping("/user")
public class UserController extends BaseController {

    @Autowired
    IUserService userService;


    /**
     * 注册
     * @return
     */
    @RequestMapping(value = "/register",method = RequestMethod.POST)
    private ApiResult<AuthInfoDTO> register(@RequestBody LoginDTO loginDTO) {
        AuthInfoDTO authInfoDto = userService.register(loginDTO);
        this.setCookies(TokenConstant.HEADER, authInfoDto.getToken(), TokenConstant.TOKEN_COOKIE_AGE);
        return data(authInfoDto);
    }

    /**
     * 登陆
     *
     * @return
     */
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    private ApiResult<AuthInfoDTO> list(@RequestBody LoginDTO loginDTO) {
        AuthInfoDTO authInfoDto = userService.login(loginDTO);
        this.setCookies(TokenConstant.HEADER, authInfoDto.getToken(), TokenConstant.TOKEN_COOKIE_AGE);
        return data(authInfoDto);
    }

    /**
     * 修改密码
     *
     * @return
     */
    @RequestMapping(value = "/resetPassword",method = RequestMethod.POST)
    private ApiResult resetPassword(@RequestBody UserPasswordDTO userPasswordDTO) {
        userService.resetPassword(userPasswordDTO);
        return success();
    }


}
