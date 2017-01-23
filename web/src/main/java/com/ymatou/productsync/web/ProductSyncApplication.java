package com.ymatou.productsync.web;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.dao.PersistenceExceptionTranslationAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.JpaBaseConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@EnableAutoConfiguration
		(exclude = {DataSourceAutoConfiguration.class
				,DataSourceTransactionManagerAutoConfiguration.class
				,JpaBaseConfiguration.class, HibernateJpaAutoConfiguration.class, PersistenceExceptionTranslationAutoConfiguration.class
				, MongoAutoConfiguration.class, MongoDataAutoConfiguration.class
		})
@MapperScan(basePackages = {"com.ymatou.productsync.domain.sqlrepo"})
@ImportResource({"classpath:spring/dubbo-provider.xml","classpath:datasource-disconf.xml"})
@ComponentScan("com.ymatou")
public class ProductSyncApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(ProductSyncApplication.class);
		app.run(args);
	}
}
