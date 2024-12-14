package com.work.ai.controller.user;

import com.work.ai.entity.dto.*;
import com.work.ai.constants.ApiResult;
import com.work.ai.controller.base.BaseController;
import com.work.ai.service.IUserService;
import com.work.ai.constants.TokenConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    public ApiResult<AuthInfoDTO> register(@RequestBody LoginDTO loginDTO) {
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
    public ApiResult<AuthInfoDTO> list(@RequestBody LoginDTO loginDTO) {
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
    public ApiResult resetPassword(@RequestBody UserPasswordDTO userPasswordDTO) {
        userService.resetPassword(userPasswordDTO);
        return success();
    }

    @RequestMapping(value = "/wxLogin",method = RequestMethod.POST)
    public ApiResult<AuthInfoDTO> wxLogin(@RequestBody WxLoginDTO wxLoginDTO) {
        AuthInfoDTO authInfoDto = userService.wxLogin(wxLoginDTO);
        this.setCookies(TokenConstant.HEADER, authInfoDto.getToken(), TokenConstant.TOKEN_COOKIE_AGE);
        return data(authInfoDto);
    }

    @RequestMapping(value = "/uploadAvatar",method = RequestMethod.POST)
    public ApiResult<Boolean> uploadAvatar(MultipartFile avatar) {
        return data(userService.uploadAvatar(avatar));
    }


    @RequestMapping(value = "/getUserDetail",method = RequestMethod.GET)
    public ApiResult<UserDTO> getUserDetail() {
        return data(userService.getUserByOpenId());
    }

    @RequestMapping(value = "/addNumber",method = RequestMethod.POST)
    public ApiResult<UserDTO> addNumber() {
        return data(userService.addNumber());
    }


}
