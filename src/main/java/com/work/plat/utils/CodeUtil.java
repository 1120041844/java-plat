package com.work.plat.utils;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.Collections;

public class CodeUtil {

    public static void main(String[] args) {

        String url = "jdbc:mysql://124.222.239.2:3306/demo?useUnicode=true&characterEncoding=UTF-8&allowMultiQueries=true&useSSL=false";
        String username = "xjn";
        String password = "123456";


        FastAutoGenerator.create(url, username, password)
                .globalConfig(builder -> {
                    builder.author("") // 设置作者
                            .enableSwagger() // 开启 swagger 模式
                            .fileOverride() // 覆盖已生成文件
                            .outputDir("/Users/jianan/Documents/plat/src/main/java/"); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("com.work") // 设置父包名
                            .moduleName("plat") // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.xml, "/Users/jianan/Documents/plat/src/main/resources/mapper/")); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude("table3") // 设置需要生成的表名
//                            .addTablePrefix("t_", "c_")
                    ; // 设置过滤表前缀
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();


    }
}
