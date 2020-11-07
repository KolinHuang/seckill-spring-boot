package com.yucaihuang.seckillspringboot;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(annotationClass = Mapper.class, basePackages = "com.yucaihuang.seckillspringboot.dao")
public class SeckillSpringBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(SeckillSpringBootApplication.class, args);
	}

}
