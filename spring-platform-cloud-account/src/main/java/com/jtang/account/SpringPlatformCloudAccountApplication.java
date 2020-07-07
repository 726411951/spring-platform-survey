package com.jtang.account;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author jtang
 * @date 2020/4/15 14:25
 */
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan(basePackages = "com.jtang.*.mapper")
public class SpringPlatformCloudAccountApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringPlatformCloudAccountApplication.class, args);
    }

}