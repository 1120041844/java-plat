package com.work.plat.controller;

import com.work.plat.constants.ApiResult;
import com.work.plat.entity.Table1;
import com.work.plat.service.ITable1Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

import java.util.List;


@Controller
@RequestMapping("/table1")
public class Table1Controller {

    @Autowired
    ITable1Service table1Service;

    @RequestMapping("/list")
    public ApiResult<List<Table1>> list() {
        return ApiResult.data(table1Service.list());
    }

}
