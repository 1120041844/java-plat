package com.work.plat.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.work.plat.entity.User;
import com.work.plat.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: xjn
 * @Date: 2022/12/5
 */
@Service
public class UserService {

    @Autowired
    UserMapper userMapper;

    public List<User> list(User user) {
        if (user == null) {
            user = new User();
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        user.setActive("1");
        queryWrapper.setEntity(user);
        return userMapper.selectList(queryWrapper);
    }

    public User getById(String id) {
        return userMapper.selectById(id);
    }

    public User add(User user) {
        int insert = userMapper.insert(user);
        return user;
    }

    public User update(User user) {
        int insert = userMapper.updateById(user);
        return user;
    }

    public String delete(String id) {
        User user = userMapper.selectById(id);
        if (user != null) {
            user.setActive("0");
        }
        userMapper.updateById(user);
        return null;
    }
}
