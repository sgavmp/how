package com.how.tfg.data.config;

import java.sql.Driver;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

@Configuration
@Profile("test")
public class MySQLConfig {
	
	@Bean
	public DataSource postgresDBFactory() throws SQLException {
		Driver driver = new com.mysql.jdbc.Driver();
		DataSource dataSource = new SimpleDriverDataSource(driver,"jdbc:mysql://localhost:3306/how","root","");
        return dataSource;
	}
}
