package com.example.demo;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;

import cn.spr.framework.candy.registry.zookeeper.ServiceRegistry;
import cn.spr.framework.candy.server.CandyServer;




@SpringBootApplication
@ComponentScan
public class TestAppApplication {
	
	private static Logger logger = LoggerFactory.getLogger(TestAppApplication.class);
	
    @Autowired
    private Environment env;
	
	public static void main(String[] args) {
	logger.debug("ssssssssss");
//		new CandyServer("192.168.1.136:8888");
		SpringApplication.run(TestAppApplication.class, args);

	}
	
	
	

	@Bean
	CandyServer candyServer() {
		return new CandyServer(env.getProperty("candyserver.address"),new ServiceRegistry(env.getProperty("registry.address")));
	}
	

}
