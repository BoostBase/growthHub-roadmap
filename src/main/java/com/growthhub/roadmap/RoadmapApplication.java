package com.growthhub.roadmap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(basePackages = "com.growthhub.global.client")
@SpringBootApplication
public class RoadmapApplication {

	public static void main(String[] args) {
		SpringApplication.run(RoadmapApplication.class, args);
	}

}
