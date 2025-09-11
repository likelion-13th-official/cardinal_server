package com.likelionsg13th.cardinal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAsync @EnableScheduling
@SpringBootApplication
public class CardinalApplication {

	public static void main(String[] args) {
		SpringApplication.run(CardinalApplication.class, args);
	}

}
