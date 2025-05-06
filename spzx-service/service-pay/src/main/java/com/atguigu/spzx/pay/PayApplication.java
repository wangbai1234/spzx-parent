package com.atguigu.spzx.pay;

import com.atguigu.spzx.pay.properties.AlipayProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@EnableConfigurationProperties(value = {AlipayProperties.class})
@SpringBootApplication
@EnableFeignClients(basePackages = {"com.atguigu.spzx"})
@ComponentScan(basePackages = {"com.atguigu.spzx"})
public class PayApplication {

    public static void main(String[] args) {
        SpringApplication.run(PayApplication.class , args) ;
    }

}
