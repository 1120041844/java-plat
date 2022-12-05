package com.work.plat;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@MapperScan("com.work.plat.mapper")
@SpringBootApplication
public class PlatApplication {

    public static void main(String[] args) {
        SpringApplication.run(PlatApplication.class, args);
    }

}
