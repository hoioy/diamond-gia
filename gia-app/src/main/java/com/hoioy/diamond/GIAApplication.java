package com.hoioy.diamond;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = {"com.hoioy.diamond.log.mapper","com.hoioy.diamond.oauth2.mapper"})
public class GIAApplication {
    public static void main(String[] args) {
        SpringApplication.run(GIAApplication.class, args);
    }
}
