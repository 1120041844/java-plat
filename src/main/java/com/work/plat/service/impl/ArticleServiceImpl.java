package com.work.plat.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.work.plat.entity.Article;
import com.work.plat.mapper.ArticleMapper;
import com.work.plat.service.IArticleService;
import org.springframework.stereotype.Service;


@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements IArticleService {

}
