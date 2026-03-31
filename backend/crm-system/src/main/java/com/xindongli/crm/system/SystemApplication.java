package com.xindongli.crm.system;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 系统管理服务启动类
 * 
 * @author 芯动力科技
 */
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.xindongli.crm.system.mapper")
public class SystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(SystemApplication.class, args);
        System.out.println("==========================================");
        System.out.println("        系统管理服务启动成功!");
        System.out.println("==========================================");
    }

}
