package com.how.tfg;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.how.tfg.modules.RefreshMeasureTask;
import com.how.tfg.modules.trello.repository.BoardMeasureRepository;

@Configuration
@ComponentScan
@EnableAutoConfiguration(exclude=ThymeleafAutoConfiguration.class)
@EnableJpaRepositories("com.how.tfg.data.repository")
@EnableMongoRepositories("com.how.tfg.modules")
@EnableMongoAuditing
public class Application implements CommandLineRunner{
	
    
	@Autowired
	BoardMeasureRepository repository;
	
	@Autowired
	Environment env;

    public static void main(String[] args) {
    	String webPort = System.getenv("PORT");
        if (webPort == null || webPort.isEmpty()) {
            webPort = "8080";
        }
        System.setProperty("server.port", webPort);
        List<Object> start = new ArrayList<Object>();
        start.add(Application.class);
        start.add(RefreshMeasureTask.class);
        SpringApplication.run(start.toArray(), args);
    }

    @Override
    public void run(String... args) throws Exception {
    	System.out.println("Deploy on " + env.getProperty("name"));
    }

}
