package com.work.plat.controller.user;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.work.plat.constants.ApiResult;
import com.work.plat.controller.base.BaseController;
import com.work.plat.entity.Dict;
import com.work.plat.entity.Menu;
import com.work.plat.mapper.DictMapper;
import com.work.plat.service.IMenuService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/menu")
public class MenuController extends BaseController {

    @Resource
    private IMenuService menuService;

    @Resource
    private DictMapper dictMapper;

    // 新增或者更新
    @PostMapping
    public ApiResult save(@RequestBody Menu menu) {
        menuService.saveOrUpdate(menu);
        return success();
    }

    @DeleteMapping("/{id}")
    public ApiResult delete(@PathVariable Integer id) {
        menuService.removeById(id);
        return success();
    }

    @PostMapping("/del/batch")
    public ApiResult deleteBatch(@RequestBody List<Integer> ids) {
        menuService.removeByIds(ids);
        return success();
    }

    @GetMapping("/ids")
    public ApiResult findAllIds() {
        return data(menuService.list().stream().map(Menu::getId));
    }

    @GetMapping
    public ApiResult<List<Menu>> findAll(@RequestParam(defaultValue = "") String name) {
        return data(menuService.findMenus(name));
    }

    @GetMapping("/{id}")
    public ApiResult<Menu> findOne(@PathVariable Integer id) {
        return data(menuService.getById(id));
    }

    @GetMapping("/page")
    public ApiResult findPage(@RequestParam String name,
                           @RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name", name);
        queryWrapper.orderByDesc("id");
        return data(menuService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

    @GetMapping("/icons")
    public ApiResult getIcons() {
        QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type", "icon");
        return data(dictMapper.selectList(queryWrapper));
    }

}

