package com.example.dulumi.Repository;

import com.example.dulumi.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByProviderId(String providerId);
    boolean existsById(Long id);
}
