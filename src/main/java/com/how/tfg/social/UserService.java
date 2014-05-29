package com.how.tfg.social;

import org.springframework.social.connect.Connection;

import com.how.tfg.data.domain.User;

public interface UserService {
	
	public User registerNewUserAccountOrGet(Connection<?> connection);
	public void measureStateOfServer();

}
