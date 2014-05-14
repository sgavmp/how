package com.how.tfg.social;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.UserProfile;

import com.how.tfg.data.domain.User;

public interface UserService {
	
	public User registerNewUserAccountOrGet(Connection<?> connection);

}
