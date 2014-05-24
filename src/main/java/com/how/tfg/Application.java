package com.how.tfg;

import javax.sql.DataSource;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.how.tfg.data.domain.User;
import com.how.tfg.data.domain.enumerate.Role;
import com.how.tfg.data.domain.trello.BoardMeasure;
import com.how.tfg.data.domain.trello.BoardMeasureRepository;
import com.how.tfg.data.repository.UserRepository;

@Configuration
@ComponentScan
@EnableAutoConfiguration(exclude=ThymeleafAutoConfiguration.class)
@EnableJpaRepositories("com.how.tfg.data.repository")
@EnableMongoRepositories("com.how.tfg.data.domain.trello")
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
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
    	System.out.println("Deploy on " + env.getProperty("name"));
    	BoardMeasure trello = new BoardMeasure();
    	trello.setEmail("sga.vmp@gmail.com");
    	trello.setDateCreation(DateTime.now());
    	trello.setName("Nombre del tablero");
    	trello.setBoardId("asda3radfa3f323asdf");
    	repository.save(trello);
    }

}
