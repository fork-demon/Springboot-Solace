package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jms.JmsAutoConfiguration;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication(exclude=JmsAutoConfiguration.class)
@ImportResource({"classpath*:camel-context.xml"})
public class SpringBootCamelWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootCamelWebApplication.class, args);
	}
}
