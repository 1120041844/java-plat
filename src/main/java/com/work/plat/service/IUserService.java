package com.work.plat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.work.plat.entity.User;
import com.work.plat.vo.LoginDto;
import com.work.plat.vo.RegisterUser;


public interface IUserService extends IService<User> {

    Boolean register(RegisterUser registerUser);

    User login(LoginDto loginDto);
}
