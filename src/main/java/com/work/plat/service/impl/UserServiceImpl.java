package com.work.plat.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.work.plat.constants.ResultCodeEnum;
import com.work.plat.entity.Menu;
import com.work.plat.entity.User;
import com.work.plat.entity.dto.UserDTO;
import com.work.plat.entity.dto.UserPasswordDTO;
import com.work.plat.exception.DataException;
import com.work.plat.mapper.RoleMapper;
import com.work.plat.mapper.RoleMenuMapper;
import com.work.plat.mapper.UserMapper;
import com.work.plat.service.IMenuService;
import com.work.plat.service.IUserService;
import com.work.plat.entity.dto.LoginDTO;
import com.work.plat.entity.dto.RegisterDTO;
import com.work.plat.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Resource
    private UserMapper userMapper;

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private RoleMenuMapper roleMenuMapper;

    @Resource
    private IMenuService menuService;

    @Override
    public UserDTO login(UserDTO userDTO) {
        QueryWrapper<User> queryWrapper = null;
        User userInfo = null;
        String username = userDTO.getUsername();
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
        boolean matches = passwordEncoder.matches(userDTO.getPassword(), userInfo.getPassword());
        if (!matches) {
            throw new DataException("密码错误");
        }
        BeanUtil.copyProperties(userInfo, userDTO, true);
        // 设置token
        String token = TokenUtils.genToken(userInfo.getId().toString(), userInfo.getPassword());
        userDTO.setToken(token);

        String role = userInfo.getRole(); // ROLE_ADMIN
        // 设置用户的菜单列表
        List<Menu> roleMenus = getRoleMenus(role);
        userDTO.setMenus(roleMenus);
        return userDTO;

    }

    @Override
    public User register(UserDTO userDTO) {
        String username = userDTO.getUsername();
        QueryWrapper<User> queryWrapper = null;
        if (StrUtil.isNotEmpty(username)) {
            queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("username",username);
            User one = this.getOne(queryWrapper);
            if (ObjectUtil.isNotEmpty(one)) {
                throw new DataException("账户:{"+ username +"}已被注册");
            }
        }

        User user = new User();
        BeanUtil.copyProperties(userDTO,user);

        String password = userDTO.getPassword();
        if (StrUtil.isNotEmpty(password)) {
            String passwordEncode = passwordEncoder.encode(password);
            user.setPassword(passwordEncode);
        } else {
            // 默认123456
            String passwordEncode = passwordEncoder.encode("123456");
            user.setPassword(passwordEncode);
        }
        boolean save = super.save(user);
        return user;
    }

    @Override
    public void resetPassword(UserPasswordDTO userPasswordDTO) {
        int update = userMapper.updatePassword(userPasswordDTO);
        if (update < 1) {
            throw new DataException(ResultCodeEnum.FAIL.getCode(), "密码错误");
        }
    }


    private List<Menu> getRoleMenus(String roleFlag) {
        Integer roleId = roleMapper.selectByFlag(roleFlag);
        // 当前角色的所有菜单id集合
        List<Integer> menuIds = roleMenuMapper.selectByRoleId(roleId);

        // 查出系统所有的菜单(树形)
        List<Menu> menus = menuService.findMenus("");
        // new一个最后筛选完成之后的list
        List<Menu> roleMenus = new ArrayList<>();
        // 筛选当前用户角色的菜单
        for (Menu menu : menus) {
            if (menuIds.contains(menu.getId())) {
                roleMenus.add(menu);
            }
            List<Menu> children = menu.getChildren();
            // removeIf()  移除 children 里面不在 menuIds集合中的 元素
            children.removeIf(child -> !menuIds.contains(child.getId()));
        }
        return roleMenus;
    }
}
