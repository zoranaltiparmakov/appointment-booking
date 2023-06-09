package com.altiparmakov.apigateway.repository;

import org.springframework.data.repository.CrudRepository;
import com.altiparmakov.apigateway.domain.User;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);
}
