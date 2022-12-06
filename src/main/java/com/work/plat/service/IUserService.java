package com.work.plat.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.work.plat.entity.User;
import com.work.plat.entity.dto.UserDTO;
import com.work.plat.entity.dto.UserPasswordDTO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 青哥哥
 * @since 2022-01-26
 */
public interface IUserService extends IService<User> {

    UserDTO login(UserDTO userDTO);

    User register(UserDTO userDTO);


    void resetPassword(UserPasswordDTO userPasswordDTO);
}
