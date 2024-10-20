package com.work.plat.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.work.plat.entity.bo.UserDO;
import com.work.plat.entity.dto.*;
import org.springframework.web.multipart.MultipartFile;

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

    AuthInfoDTO wxLogin(WxLoginDTO wxLoginDTO);


    AuthInfoDTO register(LoginDTO loginDTO);

    void resetPassword(UserPasswordDTO userPasswordDTO);

    boolean checkUser(UserDTO userDTO);

    Boolean uploadAvatar(MultipartFile avatar);
}
