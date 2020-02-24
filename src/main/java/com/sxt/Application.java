package com.sxt;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
/*
Springboot2.1.5集成activiti7.1.24出现登录验证
* https://blog.csdn.net/youjiangtengwan1/article/details/100126672
* */
@SpringBootApplication( exclude = {
		org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
		org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration.class
})
@MapperScan(basePackages= {"com.sxt.*.mapper"})
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
