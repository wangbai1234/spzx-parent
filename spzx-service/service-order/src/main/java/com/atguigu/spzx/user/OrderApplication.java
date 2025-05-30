package com.atguigu.spzx.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
@ComponentScan(basePackages = {"com.atguigu.spzx"})
@SpringBootApplication
@EnableFeignClients(basePackages = {"com.atguigu.spzx"})
public class OrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class , args) ;
    }

}