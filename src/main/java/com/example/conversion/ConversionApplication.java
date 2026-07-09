package com.example.conversion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@EnableKafka
@SpringBootApplication
public class ConversionApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConversionApplication.class, args);

	}

}
