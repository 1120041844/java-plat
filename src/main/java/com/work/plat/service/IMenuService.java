package com.work.plat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.work.plat.entity.Menu;

import java.util.List;


public interface IMenuService extends IService<Menu> {

    List<Menu> findMenus(String name);
}
