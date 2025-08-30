package com.bfkt.forum;

import cn.hutool.extra.spring.EnableSpringUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Tag(name = "认证服务")
@EnableSpringUtil
@SpringBootApplication
public class MainApp {
    public static void main(String[] args) {
        SpringApplication.run(MainApp.class, args);
    }
}
