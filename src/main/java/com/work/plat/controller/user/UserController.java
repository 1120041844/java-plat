package com.work.plat.controller.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.work.plat.constants.ApiResult;
import com.work.plat.controller.base.BaseController;
import com.work.plat.entity.User;
import com.work.plat.entity.dto.UserDTO;
import com.work.plat.entity.dto.UserPasswordDTO;
import com.work.plat.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Author: xjn
 * @Date: 2022/12/5
 */
@RestController
@RequestMapping("/user")
public class UserController extends BaseController {

    @Autowired
    IUserService userService;


    /**
     * 注册
     * @return
     */
    @RequestMapping(value = "/register",method = RequestMethod.POST)
    private ApiResult<User> register(@RequestBody UserDTO userDTO) {
        return data(userService.register(userDTO));
    }

    /**
     * 登陆
     *
     * @return
     */
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    private ApiResult<UserDTO> list(@RequestBody UserDTO userDTO) {
        return data(userService.login(userDTO));
    }

    /**
     * 登陆
     *
     * @return
     */
    @RequestMapping(value = "/password",method = RequestMethod.POST)
    private ApiResult resetPassword(@RequestBody UserPasswordDTO userPasswordDTO) {
        userService.resetPassword(userPasswordDTO);
        return success();
    }



    @RequestMapping(value = "/list",method = RequestMethod.GET)
    private ApiResult<List<User>> list(@RequestBody Map<String,Object> map) {
        return data(userService.listByMap(map));
    }

    @RequestMapping(value = "/getById",method = RequestMethod.GET)
    private ApiResult<User> getById(@RequestParam("id") String id) {
        return data(userService.getById(id));
    }

    @RequestMapping(value = "/add",method = RequestMethod.POST)
    private ApiResult add(@RequestBody User user) {
        userService.save(user);
        return success();
    }

    @RequestMapping(value = "/update",method = RequestMethod.POST)
    private ApiResult update(@RequestBody User user) {
        userService.updateById(user);
        return success();
    }

    @RequestMapping(value = "/delete",method = RequestMethod.GET)
    private ApiResult delete(@RequestParam("id") String id) {
        userService.removeById(id);
        return success();
    }

    @GetMapping("/role/{role}")
    public ApiResult<List<User>> findUsersByRole(@PathVariable String role) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role", role);
        List<User> list = userService.list(queryWrapper);
        return data(list);
    }

    @GetMapping("/username/{username}")
    public ApiResult<User> findByUsername(@PathVariable String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        return data(userService.getOne(queryWrapper));
    }

    @GetMapping("/page")
    public ApiResult findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam(defaultValue = "") String username,
                           @RequestParam(defaultValue = "") String email,
                           @RequestParam(defaultValue = "") String address) {

        return data(userService.findPage(new Page<>(pageNum, pageSize), username, email, address));
    }


}
