package com.work.plat.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.work.plat.constants.ResultCodeEnum;
import com.work.plat.entity.User;
import com.work.plat.exception.DataException;
import com.work.plat.mapper.UserMapper;
import com.work.plat.service.IUserService;
import com.work.plat.vo.LoginDto;
import com.work.plat.vo.RegisterUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;



@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Boolean register(RegisterUser registerUser) {
        String username = registerUser.getUsername();
        QueryWrapper<User> queryWrapper = null;
        if (StrUtil.isNotEmpty(username)) {
            queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("username",username);
            User one = this.getOne(queryWrapper);
            if (ObjectUtil.isNotEmpty(one)) {
                throw new DataException("账户:{"+ username +"}已被注册");
            }
        }

        String mobile = registerUser.getMobile();
        if (StrUtil.isNotEmpty(mobile)) {
            queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("mobile",mobile);
            User one = this.getOne(queryWrapper);
            if (ObjectUtil.isNotEmpty(one)) {
                throw new DataException("手机号:{"+ mobile +"}已被注册");
            }
        }

        User user = new User();
        BeanUtil.copyProperties(registerUser,user);

        String password = registerUser.getPassword();
        if (StrUtil.isNotEmpty(password)) {
            String passwordEncode = passwordEncoder.encode(password);
            user.setPassword(passwordEncode);
        } else {
            // 默认123456
            String passwordEncode = passwordEncoder.encode("123456");
            user.setPassword(passwordEncode);
        }
        return super.save(user);
    }

    @Override
    public User login(LoginDto loginDto) {
        QueryWrapper<User> queryWrapper = null;
        User userInfo = null;
        String username = loginDto.getUsername();
        String mobile = loginDto.getMobile();
        if (StrUtil.isNotEmpty(username)) {
            queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("username",username);
            userInfo = this.getOne(queryWrapper);
        } else if (StrUtil.isNotEmpty(mobile)) {
            queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("mobile",mobile);
            userInfo = this.getOne(queryWrapper);
        } else {
            throw new DataException("请使用账号或者手机号登陆");
        }
        if (ObjectUtil.isEmpty(userInfo)) {
            throw new DataException("账号不存在");
        }
        // 验证密码
        boolean matches = passwordEncoder.matches(loginDto.getPassword(), userInfo.getPassword());
        if (!matches) {
            throw new DataException("密码错误");
        }
        return userInfo;
    }
}
