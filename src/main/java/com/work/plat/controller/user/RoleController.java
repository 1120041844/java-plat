package com.work.plat.controller.user;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.work.plat.constants.ApiResult;
import com.work.plat.controller.base.BaseController;
import com.work.plat.entity.Role;
import com.work.plat.service.IRoleService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 青哥哥
 * @since 2022-02-10
 */
@RestController
@RequestMapping("/role")
public class RoleController extends BaseController {

    @Resource
    private IRoleService roleService;

    // 新增或者更新
    @PostMapping
    public ApiResult save(@RequestBody Role role) {
        roleService.saveOrUpdate(role);
        return success();
    }

    @DeleteMapping("/{id}")
    public ApiResult delete(@PathVariable Integer id) {
        roleService.removeById(id);
        return success();
    }

    @PostMapping("/del/batch")
    public ApiResult deleteBatch(@RequestBody List<Integer> ids) {
        roleService.removeByIds(ids);
        return success();
    }

    @GetMapping
    public ApiResult<List<Role>> findAll() {
        return data(roleService.list());
    }

    @GetMapping("/{id}")
    public ApiResult<Role> findOne(@PathVariable Integer id) {
        return data(roleService.getById(id));
    }

    @GetMapping("/page")
    public ApiResult<Page<Role>> findPage(@RequestParam String name,
                           @RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name", name);
        queryWrapper.orderByDesc("id");
        return data(roleService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

    /**
     * 绑定角色和菜单的关系
     * @param roleId 角色id
     * @param menuIds 菜单id数组
     * @return
     */
    @PostMapping("/roleMenu/{roleId}")
    public ApiResult roleMenu(@PathVariable Integer roleId, @RequestBody List<Integer> menuIds) {
        roleService.setRoleMenu(roleId, menuIds);
        return success();
    }

    @GetMapping("/roleMenu/{roleId}")
    public ApiResult<List<Integer>> getRoleMenu(@PathVariable Integer roleId) {
        return data(roleService.getRoleMenu(roleId));
    }

}

