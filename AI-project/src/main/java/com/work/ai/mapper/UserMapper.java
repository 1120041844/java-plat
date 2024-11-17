package com.work.ai.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.work.ai.entity.bo.UserDO;
import com.work.ai.entity.dto.UserDTO;
import com.work.ai.entity.dto.UserPasswordDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper extends BaseMapper<UserDO> {

    UserDO selectByOpenId(@Param("openId") String openId);

    int updatePassword(UserPasswordDTO userPasswordDTO);

    Page<UserDO> findPage(Page<UserDO> page, @Param("username") String username, @Param("email") String email, @Param("address") String address);

    UserDTO selectUserByOpenId(@Param("openId") String openId);
}
