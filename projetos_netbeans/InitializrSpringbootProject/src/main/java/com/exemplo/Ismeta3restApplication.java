package com.exemplo;

import org.aspectj.apache.bcel.Constants;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class Ismeta3restApplication {

	public static void main(String[] args) {
		SpringApplication.run(Ismeta3restApplication.class, args);
	}

}
