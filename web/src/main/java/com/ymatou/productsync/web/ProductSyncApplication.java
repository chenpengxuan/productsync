package com.ymatou.productsync.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource("classpath:spring/dubbo-provider.xml")
@ComponentScan("com.ymatou")
public class ProductSyncApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(ProductSyncApplication.class);
		app.run(args);
	}
}
