package com.how.tfg.data.config;

import javax.sql.DataSource;

import org.springframework.cloud.config.java.AbstractCloudConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("prod")
public class HerokuConfig extends AbstractCloudConfig  {
	
	@Bean
	public DataSource postgresDBFactory() {
		return connectionFactory().dataSource();
	}

}
