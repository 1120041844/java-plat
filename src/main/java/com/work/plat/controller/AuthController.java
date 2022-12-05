package com.work.plat.controller;

import com.work.plat.constants.ApiResult;
import com.work.plat.entity.User;
import com.work.plat.service.IUserService;
import com.work.plat.vo.LoginDto;
import com.work.plat.vo.RegisterUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/auth")
public class AuthController extends BaseController{

    @Autowired
    private IUserService userService;

    /**
     * 注册
     * @return
     */
    @RequestMapping(value = "/register",method = RequestMethod.POST)
    private ApiResult<Boolean> register(@RequestBody RegisterUser registerUser) {
        return data(userService.register(registerUser));
    }

    /**
     * 登陆
     *
     * @return
     */
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    private ApiResult<User> list(@RequestBody LoginDto loginDto) {
        return data(userService.login(loginDto));
    }
}
