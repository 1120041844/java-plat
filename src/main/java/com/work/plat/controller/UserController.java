package com.work.plat.controller;

import com.work.plat.constants.ApiResult;
import com.work.plat.entity.User;
import com.work.plat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: xjn
 * @Date: 2022/12/5
 */
@RestController
@RequestMapping("/user")
public class UserController extends BaseController {

    @Autowired
    UserService userService;


    @RequestMapping(value = "/list",method = RequestMethod.GET)
    private ApiResult<List<User>> list(@RequestBody User user) {
        return data(userService.list(user));
    }

    @RequestMapping(value = "/getById",method = RequestMethod.GET)
    private ApiResult<User> getById(@RequestParam("id") String id) {
        return data(userService.getById(id));
    }

    @RequestMapping(value = "/add",method = RequestMethod.POST)
    private ApiResult add(@RequestBody User user) {
        userService.add(user);
        return success();
    }

    @RequestMapping(value = "/update",method = RequestMethod.POST)
    private ApiResult update(@RequestBody User user) {
        userService.update(user);
        return success();
    }

    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    private ApiResult delete(@RequestParam("id") String id) {
        userService.delete(id);
        return success();
    }


}
