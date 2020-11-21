package com.zt.djt_shardingsphere;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.zt.djt_shardingsphere.mapper")
public class DjtShardingsphereApplication {

    public static void main(String[] args) {
        SpringApplication.run(DjtShardingsphereApplication.class, args);
    }

}
