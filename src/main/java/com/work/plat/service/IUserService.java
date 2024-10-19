package com.work.plat.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.work.plat.entity.bo.UserDO;
import com.work.plat.entity.dto.AuthInfoDTO;
import com.work.plat.entity.dto.LoginDTO;
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
public interface IUserService extends IService<UserDO> {

    AuthInfoDTO login(LoginDTO loginDTO);

    AuthInfoDTO register(LoginDTO loginDTO);

    void resetPassword(UserPasswordDTO userPasswordDTO);

    boolean checkUser(UserDTO userDTO);

}
