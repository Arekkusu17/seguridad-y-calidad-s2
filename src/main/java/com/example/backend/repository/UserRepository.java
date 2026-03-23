package com.example.backend.repository;

import org.springframework.data.repository.CrudRepository;
import com.example.backend.model.User;

public interface UserRepository extends CrudRepository<User, Integer> {
    User findByUsername(String username);

}
