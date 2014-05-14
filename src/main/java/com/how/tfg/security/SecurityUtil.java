package com.how.tfg.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.how.tfg.data.domain.User;
import com.how.tfg.data.domain.UserDetails;

public class SecurityUtil {
	
	public static void logInUser(User user) {
        UserDetails userDetails = UserDetails.getBuilder()
                .firstName(user.getFirstName())
                .id(user.getId())
                .lastName(user.getLastName())
                .role(user.getRole())
                .username(user.getEmail())
                .build();

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

}
