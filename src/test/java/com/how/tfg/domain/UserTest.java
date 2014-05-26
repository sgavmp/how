package com.how.tfg.domain;


import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.how.tfg.Application;
import com.how.tfg.data.domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class UserTest {
	
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
