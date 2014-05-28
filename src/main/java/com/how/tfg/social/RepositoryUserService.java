package com.how.tfg.social;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.connect.UserProfile;
import org.springframework.stereotype.Service;

import com.how.tfg.data.domain.User;
import com.how.tfg.data.repository.UserRepository;
import com.how.tfg.data.domain.enumerate.SocialMediaService;

@Service
public class RepositoryUserService implements UserService {

	private UserRepository repository;

	@Autowired
	public RepositoryUserService(UserRepository repository) {
		this.repository = repository;
	}

	@Transactional
	@Override
	public User registerNewUserAccountOrGet(Connection<?> connection) {
		UserProfile socialMediaProfile = connection.fetchUserProfile();

		User u = repository.findByEmail(socialMediaProfile.getEmail());

		if (u == null) {

			User.Builder user = User.getBuilder()
					.email(socialMediaProfile.getEmail())
					.firstName(socialMediaProfile.getFirstName())
					.lastName(socialMediaProfile.getLastName())
					.imageProfile(connection.getImageUrl());

			User registered = user.build();

			return repository.save(registered);
			
		} else {
			
			return u;
			
		}
	}

}
