package com.how.tfg;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.social.connect.Connection;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.how.tfg.data.domain.User;
import com.how.tfg.data.service.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {Application.class})
@WebAppConfiguration
@IntegrationTest("server.port=0")
@DirtiesContext
public class UserServiceTest {

	@Mock
	private UserService service;
	
	@Mock
	private User user;
	@Mock
	private Connection<?> connection;

	// Start method
	// ---------------------------------------------------------------
	@Before
	public void setUp() {
		 MockitoAnnotations.initMocks(this);
	}

	// Test methods
	// ---------------------------------------------------------------
	// Positive test methods
	// -----------------------------------------------------

	@Test
	public void registerNewUser() {
		when(service.registerNewUserAccountOrGet(connection)).thenReturn(user);
		
		User temp = service.registerNewUserAccountOrGet(connection);
		
        assertNotNull(temp);
        assertThat(temp, equalTo(user));
	}

}
