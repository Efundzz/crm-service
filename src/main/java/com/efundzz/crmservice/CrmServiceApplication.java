package com.efundzz.crmservice;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
public class CrmServiceApplication {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.load();
		dotenv.entries().forEach(entry -> {
			System.getProperties().putIfAbsent(entry.getKey(), entry.getValue());
		});
		SpringApplication.run(CrmServiceApplication.class, args);
	}

}
