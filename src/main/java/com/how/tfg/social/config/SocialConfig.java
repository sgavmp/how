package com.how.tfg.social.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.social.UserIdSource;
import org.springframework.social.config.annotation.ConnectionFactoryConfigurer;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurer;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.social.dropbox.connect.TrelloConnectionFactory;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.github.connect.GitHubConnectionFactory;
import org.springframework.social.google.connect.GoogleConnectionFactory;
import org.springframework.social.security.AuthenticationNameUserIdSource;


@Configuration
@EnableSocial
public class SocialConfig implements SocialConfigurer{
	
	@Autowired
    private DataSource dataSource;

    @Override
    public void addConnectionFactories(ConnectionFactoryConfigurer cfConfig, Environment env) {
    	FacebookConnectionFactory conectionFacebook = new FacebookConnectionFactory(
                env.getProperty("facebook.app.id"),
                env.getProperty("facebook.app.secret")
        );
    	GoogleConnectionFactory connectionGoogle = new GoogleConnectionFactory(
    			env.getProperty("google.app.id"),
                env.getProperty("google.app.secret")
        );
    	TrelloConnectionFactory connectionTrello = new TrelloConnectionFactory(
    			env.getProperty("trello.app.id"),
                env.getProperty("trello.app.secret")
        );
    	GitHubConnectionFactory connectionGithub = new GitHubConnectionFactory(
    			env.getProperty("github.app.id"),
                env.getProperty("github.app.secret")
        );
    	cfConfig.addConnectionFactory(conectionFacebook);
    	cfConfig.addConnectionFactory(connectionGoogle);
    	cfConfig.addConnectionFactory(connectionTrello);
    	cfConfig.addConnectionFactory(connectionGithub);
    }

    @Override
    public UserIdSource getUserIdSource() {
        return new AuthenticationNameUserIdSource();
    }

    @Override
    public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
        return new JdbcUsersConnectionRepository(
                dataSource,
                connectionFactoryLocator,
                Encryptors.noOpText()
        );
    }
    
}
