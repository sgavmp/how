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
import org.springframework.social.security.SocialUserDetailsService;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.how.tfg.data.domain.UserDetails;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {Application.class})
@WebAppConfiguration
@IntegrationTest("server.port=0")
@DirtiesContext
public class SocialUserDetailsServiceTest {

	@Mock
	private SocialUserDetailsService service;
	
	@Mock
	private UserDetails user;


	// Start method
	// ---------------------------------------------------------------
	@Before
	public void setUp() {
		 MockitoAnnotations.initMocks(this);
		 user.setId(new Long(1));
	}

	// Test methods
	// ---------------------------------------------------------------
	// Positive test methods
	// -----------------------------------------------------

	@Test
	public void getById() {
		Long idL = new Long(11);
		when(service.loadUserByUserId(idL.toString())).thenReturn(user);
		when(user.getId()).thenReturn(idL);
		
		UserDetails temp = (UserDetails) service.loadUserByUserId(idL.toString());
		temp.setId(idL);
		
        assertNotNull(temp);
        assertThat(temp.getId(), equalTo(user.getId()));
	}

}
