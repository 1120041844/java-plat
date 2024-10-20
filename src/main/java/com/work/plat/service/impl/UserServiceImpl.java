package com.work.plat.service.impl;

import ch.qos.logback.core.util.ContextUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.work.plat.constants.ResultCodeEnum;
import com.work.plat.constants.TokenConstant;
import com.work.plat.constants.WxConstant;
import com.work.plat.entity.TokenInfo;
import com.work.plat.entity.bo.UserDO;
import com.work.plat.entity.dto.*;
import com.work.plat.exception.DataException;
import com.work.plat.mapper.UserMapper;
import com.work.plat.service.IUserService;
import com.work.plat.utils.AesUtil;
import com.work.plat.utils.SecureUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
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
            queryWrapper.eq("username", username);
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
    public AuthInfoDTO wxLogin(WxLoginDTO wxLoginDTO) {
//        JSONObject data = getUserInfoByWx(wxLoginDTO);
//        if (data == null) {
//            throw new DataException("获取信息失败,请稍后再试");
//        }
//        //获取会话密钥（session_key）
//        String session_key = data.getString("session_key");
        //用户的唯一标识（openid）
//        String openid = data.getString("openid");
        String openid = "testopenId";

        try {
            UserDO userDO = userMapper.selectByOpenId(openid);
            if (userDO == null) {
                userDO = new UserDO();
                userDO.setUsername("userName");
                userDO.setOpenId(openid);
                // 默认123456
                String passwordEncode = passwordEncoder.encode("123456");
                userDO.setPassword(passwordEncode);
                userDO.setCreateTime(new Date());
                userDO.setNickname(UUID.randomUUID().toString());
                this.save(userDO);
            }
            UserDTO userDTO = BeanUtil.copyProperties(userDO, UserDTO.class);
            Map<String, Object> param = BeanUtil.beanToMap(userDTO);
            // 设置token
            TokenInfo accessToken = SecureUtil.createJWT(param, "audience", "issuser", TokenConstant.HEADER);

            AuthInfoDTO authInfoDto = new AuthInfoDTO();
            authInfoDto.setUserDTO(userDTO);
            authInfoDto.setToken(accessToken.getToken());
            authInfoDto.setExpiresIn(accessToken.getExpire());

            return authInfoDto;
        } catch (Exception e) {
            log.error("账户注册失败:", e);
            throw new DataException("账户注册失败");
        }
    }

    private JSONObject getUserInfoByWx(WxLoginDTO wxLoginDTO) {
        String code = wxLoginDTO.getCode();
//        String encData = wxLoginDTO.getEncData();
//        String iv = wxLoginDTO.getIv();
        if (StrUtil.isEmpty(code)) {
            throw new DataException("code 不能为空");
        }

        Map<String,Object> params = new HashMap<>();
        params.put("appid",WxConstant.appId);
        params.put("secret",WxConstant.secret);
        params.put("js_code",code);
        params.put("grant_type","authorization_code");
        log.info("request code:{}",code);
        String response = HttpUtil.get("https://api.weixin.qq.com/sns/jscode2session", params);
        log.info("response info:{}",response);

        //解析相应内容（转换成json对象）
        JSONObject json = JSONObject.parseObject(response);
        //获取会话密钥（session_key）
        String session_key = json.getString("session_key");
        //用户的唯一标识（openid）
        String openid =json.getString("openid");
//        String decrypt;
//        try {
//            decrypt = AesUtil.decrypt(encData, session_key, iv, "UTF-8");
//        } catch (Exception e) {
//            log.error("解码失败:", e);
//            throw new DataException("解码失败 请稍后再试");
//        }
//        log.info("decrypt success,data:{}", decrypt);
//        Map data = JSONObject.parseObject(decrypt, Map.class);

        return json;
    }


    @Override
    public AuthInfoDTO register(LoginDTO loginDTO) {
        String username = loginDTO.getUsername();
        QueryWrapper<UserDO> queryWrapper = null;
        if (StrUtil.isNotEmpty(username)) {
            queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("username", username);
            UserDO one = this.getOne(queryWrapper);
            if (ObjectUtil.isNotEmpty(one)) {
                throw new DataException("账户:{" + username + "}已被注册");
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

    @Override
    public Boolean uploadAvatar(MultipartFile avatar) {
        UserDTO userDTO = SecureUtil.getUser();
        if (userDTO == null) {
            throw new DataException("未找到用户信息");
        }
        String openId = userDTO.getOpenId();
        UserDO userDO = userMapper.selectByOpenId(openId);
        if (userDO == null) {
            throw new DataException("未找到用户信息");
        }
//        userDO.setAvatar(loginDTO.getAvatar());
        return super.updateById(userDO);
    }


}
