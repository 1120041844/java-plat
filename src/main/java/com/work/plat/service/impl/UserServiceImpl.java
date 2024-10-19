package com.work.plat.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.work.plat.constants.ResultCodeEnum;
import com.work.plat.constants.TokenConstant;
import com.work.plat.entity.TokenInfo;
import com.work.plat.entity.bo.UserDO;
import com.work.plat.entity.dto.*;
import com.work.plat.exception.DataException;
import com.work.plat.mapper.UserMapper;
import com.work.plat.service.IUserService;
import com.work.plat.utils.SecureUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;


@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDO> implements IUserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Resource
    private UserMapper userMapper;


    @Override
    public AuthInfoDTO login(LoginDTO loginDTO) {
        QueryWrapper<UserDO> queryWrapper = null;
        UserDO userInfo = null;
        String username = loginDTO.getUsername();
        if (StrUtil.isNotEmpty(username)) {
            queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("username",username);
            userInfo = this.getOne(queryWrapper);
        } else {
            throw new DataException("请使用账号登陆");
        }
        if (ObjectUtil.isEmpty(userInfo)) {
            throw new DataException("账号不存在");
        }
        // 验证密码
        boolean matches = passwordEncoder.matches(loginDTO.getPassword(), userInfo.getPassword());
        if (!matches) {
            throw new DataException("密码错误");
        }
        UserDTO userDTO = BeanUtil.copyProperties(userInfo, UserDTO.class);
        Map<String, Object> param = BeanUtil.beanToMap(userDTO);
        // 设置token
        TokenInfo accessToken = SecureUtil.createJWT(param, "audience", "issuser", TokenConstant.HEADER);

        AuthInfoDTO authInfoDto = new AuthInfoDTO();
        authInfoDto.setUserDTO(userDTO);
        authInfoDto.setToken(accessToken.getToken());
        authInfoDto.setExpiresIn(accessToken.getExpire());

        return authInfoDto;
    }



    @Override
    public AuthInfoDTO register(LoginDTO loginDTO) {
        String username = loginDTO.getUsername();
        QueryWrapper<UserDO> queryWrapper = null;
        if (StrUtil.isNotEmpty(username)) {
            queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("username",username);
            UserDO one = this.getOne(queryWrapper);
            if (ObjectUtil.isNotEmpty(one)) {
                throw new DataException("账户:{"+ username +"}已被注册");
            }
        }

        UserDO user = BeanUtil.copyProperties(loginDTO, UserDO.class);

        String password = loginDTO.getPassword();
        if (StrUtil.isNotEmpty(password)) {
            String passwordEncode = passwordEncoder.encode(password);
            user.setPassword(passwordEncode);
        } else {
            // 默认123456
            String passwordEncode = passwordEncoder.encode("123456");
            user.setPassword(passwordEncode);
        }
        boolean save = super.save(user);

        UserDTO userDTO = BeanUtil.copyProperties(user, UserDTO.class);
        Map<String, Object> param = BeanUtil.beanToMap(userDTO);
        // 设置token
        TokenInfo accessToken = SecureUtil.createJWT(param, "audience", "issuser", TokenConstant.HEADER);

        AuthInfoDTO authInfoDto = new AuthInfoDTO();
        authInfoDto.setUserDTO(userDTO);
        authInfoDto.setToken(accessToken.getToken());
        authInfoDto.setExpiresIn(accessToken.getExpire());
        return authInfoDto;
    }

    @Override
    public void resetPassword(UserPasswordDTO userPasswordDTO) {
        int update = userMapper.updatePassword(userPasswordDTO);
        if (update < 1) {
            throw new DataException(ResultCodeEnum.FAIL.getCode(), "密码错误");
        }
    }

    @Override
    public boolean checkUser(UserDTO userDTO) {
        Integer id = userDTO.getId();
        if (id == null) {
            return false;
        }
        UserDO userDO = super.getById(id);
        if (userDO == null) {
            return false;
        }
        // ...
        return true;
    }


}
