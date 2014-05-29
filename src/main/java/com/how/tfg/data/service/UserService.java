package com.how.tfg.data.service;

import org.springframework.social.connect.Connection;

import com.how.tfg.data.domain.User;

public interface UserService {
	
	public User registerNewUserAccountOrGet(Connection<?> connection);
	public void measureStateOfServer();

}
