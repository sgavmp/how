package com.how.tfg.security.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.social.security.SocialUserDetailsService;
import org.springframework.social.security.SpringSocialConfigurer;

import com.how.tfg.data.repository.UserRepository;
import com.how.tfg.security.RepositoryUserDetailsService;
import com.how.tfg.social.SimpleSocialUserDetailsService;

/**
 * Created by Sergio on 18/04/2014.
 */
@Configuration
@EnableWebMvcSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
    private UserRepository userRepository;
	
	@Autowired
    private DataSource dataSource;

	@Override
    protected void configure(HttpSecurity http) throws Exception {
        http
        		//Configures the rememberMe
                .rememberMe()
                .key("6772b7939386362af7ed96915")
                .rememberMeServices(persistentTokenBasedRememberMeServices())
                .and().formLogin().loginPage("/error/login")
                //Configures the logout function
                .and()
                    .logout()
                        .deleteCookies("JSESSIONID")
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                //Configures url based authorization
                .and()
                    .authorizeRequests()
                        //Anyone can access the urls
                        .antMatchers(
                                "/auth/**",
                                "/connect/**",
                                "/signin/**",
                                "/signup/**",
                                "/user/register/**",
                                "/",
                                "/main",
                                "/static/**",
                                "/webjars/**",
                                "/state/**",
                                "/error/**"
                        ).permitAll()
                        //The rest of the our application is protected.
                        .antMatchers("/**").hasRole("USER")
                .and()
               		.exceptionHandling()
               			.accessDeniedPage("/error/login")
                //Adds the SocialAuthenticationFilter to Spring Security's filter chain.
                .and()
                    .apply(new SpringSocialConfigurer());
    }

	@Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService())
                .passwordEncoder(passwordEncoder());
    }
	
	@Bean
	public PersistentTokenBasedRememberMeServices persistentTokenBasedRememberMeServices(){
		PersistentTokenBasedRememberMeServices persistentTokenBasedRememberMeServices = new PersistentTokenBasedRememberMeServices("6772b7939386362af7ed96915", userDetailsService(), jdbcTokenRepositoryImpl());
		persistentTokenBasedRememberMeServices.setAlwaysRemember(true);
		return persistentTokenBasedRememberMeServices;
	}

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public SocialUserDetailsService socialUserDetailsService() {
        return new SimpleSocialUserDetailsService(userDetailsService());
    }
    
    @Bean
    public JdbcTokenRepositoryImpl jdbcTokenRepositoryImpl() {
    	JdbcTokenRepositoryImpl jdbcTokenRepositoryImpl = new JdbcTokenRepositoryImpl();
    	jdbcTokenRepositoryImpl.setDataSource(dataSource);
//    	jdbcTokenRepositoryImpl.setCreateTableOnStartup(true); 
    	return jdbcTokenRepositoryImpl;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new RepositoryUserDetailsService(userRepository);
    }
}
