package com.work.ai.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.work.ai.constants.CommonConstant;
import com.work.ai.entity.TokenInfo;
import com.work.ai.entity.bo.SysUserRemainingDO;
import com.work.ai.entity.bo.UserDO;
import com.work.ai.entity.dto.*;
import com.work.ai.exception.DataException;
import com.work.ai.mapper.SysUserRemainingMapper;
import com.work.ai.mapper.UserMapper;
import com.work.ai.utils.Base64IdUtil;
import com.work.ai.utils.SecureUtil;
import com.work.ai.constants.ResultCodeEnum;
import com.work.ai.constants.TokenConstant;
import com.work.ai.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDO> implements IUserService {

    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private UserMapper userMapper;
    @Resource
    private SysUserRemainingMapper sysUserRemainingMapper;

    private final ReentrantLock lock = new ReentrantLock();
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
        JSONObject data = getUserInfoByWx(wxLoginDTO);
        if (data == null) {
            throw new DataException("获取登录信息失败,请稍后再试");
        }
//        //获取会话密钥（session_key）
        String session_key = data.getString("session_key");
        //用户的唯一标识（openid）
        String openid = data.getString("openid");
        if (StrUtil.isEmpty(openid)) {
            throw new DataException("登录失败，请重新进入。");
        }

        try {
            UserDO userDO = userMapper.selectByOpenId(openid);
            if (userDO == null) {
                userDO = new UserDO();
                userDO.setUsername(Base64IdUtil.generateShortId());
                userDO.setOpenId(openid);
                // 默认123456
                String passwordEncode = passwordEncoder.encode("123456");
                userDO.setPassword(passwordEncode);
                userDO.setCreateTime(new Date());
                userDO.setNickname(UUID.randomUUID().toString());
                this.save(userDO);
                // 初始化记录数
                SysUserRemainingDO userRemainingDO = new SysUserRemainingDO();
                userRemainingDO.setNumber(10L);
                userRemainingDO.setCreateTime(new Date());
                userRemainingDO.setIncreaseTime(new Date());
                userRemainingDO.setUpdateTime(new Date());
                userRemainingDO.setOpenId(openid);
                sysUserRemainingMapper.insert(userRemainingDO);
            }
            UserDTO userDTO = BeanUtil.copyProperties(userDO, UserDTO.class);
            // 额度查询
            Long number = sysUserRemainingMapper.selectNumberByOpenId(openid);
            userDTO.setNumber(number);
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
        params.put("appid", CommonConstant.wxAppId);
        params.put("secret", CommonConstant.wxSecret);
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

    @Override
    public UserDTO getUserByOpenId() {
        UserDTO userDTO = SecureUtil.getUser();
        if (userDTO == null) {
            throw new DataException("未找到用户信息");
        }
        String openId = userDTO.getOpenId();
        if (StrUtil.isEmpty(openId)) {
            throw new DataException("未找到用户信息");
        }
        return userMapper.selectUserByOpenId(openId);
    }

    @Override
    public UserDTO addNumber() {
        UserDTO userDTO = SecureUtil.getUser();
        if (userDTO == null) {
            throw new DataException("未找到用户信息");
        }
        String openId = userDTO.getOpenId();
        if (StrUtil.isEmpty(openId)) {
            throw new DataException("未找到用户信息");
        }
        // 获取锁
        boolean tryLock = lock.tryLock();
        if (!tryLock) {
            // 没获取到锁直接返回
            return userDTO;
        }
        try {
            // 查询用户剩余次数记录
            LambdaQueryWrapper<SysUserRemainingDO> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(SysUserRemainingDO::getOpenId, openId);
            SysUserRemainingDO remainingDO = sysUserRemainingMapper.selectOne(queryWrapper);
            // 如果没有找到用户的剩余次数记录，说明是第一次，直接新增记录并赋初始值
            if (remainingDO == null) {
                remainingDO = new SysUserRemainingDO();
                remainingDO.setOpenId(openId);
                remainingDO.setNumber(10L);  // 初始次数为 10
                remainingDO.setCreateTime(new Date());
                remainingDO.setIncreaseTime(new Date());
                remainingDO.setUpdateTime(new Date());  // 设置当前时间为更新时间
                sysUserRemainingMapper.insert(remainingDO);
                userDTO.setNumber(10L);
                userDTO.setToday(10L);
                return userDTO;
            }

            // 判断是否是今天更新的
            LocalDate today = LocalDate.now();
            LocalDate lastUpdateDate = remainingDO.getIncreaseTime().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            // 判断是否是今天更新的
            if (lastUpdateDate.equals(today)) {
                // 如果是今天已更新，返回错误信息
                throw new DataException("今日免费权益已领取过，请明日再试。");
            } else {
                Long number = remainingDO.getNumber() + 10;
                remainingDO.setNumber(number);
                remainingDO.setIncreaseTime(new Date());
                sysUserRemainingMapper.updateById(remainingDO);  // 更新数据库记录
                userDTO.setNumber(number);
                userDTO.setToday(10L);
                return userDTO;
            }
        } finally {
            // 无论成功还是失败，都释放锁
            lock.unlock();
        }
    }


}
