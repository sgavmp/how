package com.how.tfg.data.repository;

import org.springframework.data.repository.CrudRepository;

import com.how.tfg.data.domain.User;

/**
 * Created by Sergio on 18/04/2014.
 */

public interface UserRepository extends CrudRepository<User,String> {
    public User findByEmail(String email);
}
