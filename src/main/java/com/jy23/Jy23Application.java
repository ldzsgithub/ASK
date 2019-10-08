package com.jy23;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.jy23.mapper")
public class Jy23Application {

	public static void main(String[] args) {
		SpringApplication.run(Jy23Application.class, args);
	}

}
