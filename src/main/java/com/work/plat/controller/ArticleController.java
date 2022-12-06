package com.work.plat.controller;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.work.plat.constants.ApiResult;
import com.work.plat.controller.base.BaseController;
import com.work.plat.entity.Article;
import com.work.plat.service.IArticleService;
import com.work.plat.utils.TokenUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


@RestController
@RequestMapping("/article")
public class ArticleController extends BaseController {

    @Resource
    private IArticleService articleService;

    // 新增或者更新
    @PostMapping
    public ApiResult save(@RequestBody Article article) {
        if (article.getId() == null) { // 新增
            article.setTime(DateUtil.now());  // new Date()
            article.setUser(TokenUtils.getCurrentUser().getNickname());
        }
        articleService.saveOrUpdate(article);
        return ApiResult.success();
    }

    @DeleteMapping("/{id}")
    public ApiResult delete(@PathVariable Integer id) {
        articleService.removeById(id);
        return ApiResult.success();
    }

    @PostMapping("/del/batch")
    public ApiResult deleteBatch(@RequestBody List<Integer> ids) {
        articleService.removeByIds(ids);
        return ApiResult.success();
    }

    @GetMapping
    public ApiResult<List<Article>> findAll(@RequestParam(required = false) String start, @RequestParam(required = false) String end) {
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();

        if (StrUtil.isNotBlank(start)) {
            // where time >= start
            queryWrapper.ge("time", start);
        }
        if (StrUtil.isNotBlank(end)) {
            // where time <= end
            queryWrapper.le("time", end);
        }
        return data(articleService.list(queryWrapper));
    }

    @GetMapping("/{id}")
    public ApiResult findOne(@PathVariable Integer id) {
        return data(articleService.getById(id));
    }

    @GetMapping("/page")
    public ApiResult<Page<Article>> findPage(@RequestParam String name,
                           @RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        if (StrUtil.isNotBlank(name)) {
            queryWrapper.like("name", name);
        }
        return data(articleService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

}

