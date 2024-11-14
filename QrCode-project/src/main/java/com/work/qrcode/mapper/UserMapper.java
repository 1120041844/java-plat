package com.work.qrcode.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.work.qrcode.entity.bo.UserDO;
import com.work.qrcode.entity.dto.user.UserPasswordDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper extends BaseMapper<UserDO> {

    UserDO selectByOpenId(@Param("openId") String openId);

    int updatePassword(UserPasswordDTO userPasswordDTO);

    Page<UserDO> findPage(Page<UserDO> page, @Param("username") String username, @Param("email") String email, @Param("address") String address);

}
