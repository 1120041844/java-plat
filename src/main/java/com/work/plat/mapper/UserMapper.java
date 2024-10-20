package com.work.plat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.work.plat.entity.bo.UserDO;
import com.work.plat.entity.dto.UserPasswordDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper extends BaseMapper<UserDO> {

    @Select("select * from sys_user where open_id = #{openId}")
    UserDO selectByOpenId(@Param("openId") String openId);

    @Update("update sys_user set password = #{newPassword} where username = #{username} and password = #{password}")
    int updatePassword(UserPasswordDTO userPasswordDTO);

    Page<UserDO> findPage(Page<UserDO> page, @Param("username") String username, @Param("email") String email, @Param("address") String address);

}
