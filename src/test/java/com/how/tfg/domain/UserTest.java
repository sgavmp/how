package com.how.tfg.domain;


import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.how.tfg.Application;
import com.how.tfg.data.domain.User;
import com.how.tfg.data.repository.UserRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class UserTest {

	@Autowired
	UserRepository userRepository;
	
	@Test
	public void createUserCorrect(){
		User newuser = new User().
				getBuilder().
				email("sga.vmp@gmail.com").
				firstName("Sergio").lastName("Garcia").
				build();
		Assert.assertNotNull(newuser);
	}

}