package com.how.tfg.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.how.tfg.data.domain.User;
import com.how.tfg.data.domain.UserDetails;
import com.how.tfg.data.repository.UserRepository;

import java.util.Collection;

/**
 * Created by Sergio on 19/04/2014.
 */
@Service
public class RepositoryUserDetailsService  implements UserDetailsService {

    private UserRepository repository;
    
    @Autowired
    public RepositoryUserDetailsService(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.findByEmail(username);

        if (user == null) {
            throw new UsernameNotFoundException("No user found with username: " + username);
        }

        UserDetails principal = UserDetails.getBuilder()
                .firstName(user.getFirstName())
                .id(user.getId())
                .lastName(user.getLastName())
                .role(user.getRole())
                .username(user.getEmail())
                .build();

        return principal;
    }
}
